package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.logs.Log
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.db.models.BotConfigEntity
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.requests.CreateOrderRequest
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class PlaceBuyLimitOrders(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val configsDao: BotConfigsDao,
    private val balanceDao: BalanceDao,
    private val orderDao: OrderDao,
    private val tickersDao: TickersDao,
) : ResultInteractor<PlaceBuyLimitOrders.Params, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Unit = withContext(dispatcher) {
        val configs = configsDao.getActive().filter { it.side == Order.Side.Buy }

        configs.forEach { config ->
            if (config.orderAmount == 0 || config.orderSize == 0) return@forEach

            val quotAsset = balanceDao.getCurrency(currency = config.quoteAsset) ?: return@forEach

            val pairName = config.baseAsset + "_" + config.quoteAsset
            val startQuantity = config.minSize * config.orderSize
            val bestPrice = bestPrice(pairName, config) ?: return@forEach

            // If there is not enough quoteAsset even to purchase the minimum volume
            if (quotAsset.balance < (startQuantity * bestPrice)) return@forEach

            val balance = balanceDao.getCurrency(currency = config.baseAsset) ?: return@forEach

            val activeOrders = orderDao.get(side = Order.Side.Buy)
                .onFailure { Log.warn(it) { "Can't get active orders side=Buy from table" } }
                .getOrNull() ?: return@forEach

            if (balance.balance < config.fond) {    // need to buy
                when {
                    activeOrders.isEmpty() -> {}
                    activeOrders.size < config.orderAmount -> {}
                    activeOrders.minOf { it.price } != bestPrice -> {}
                    activeOrders.minOf { it.quantity } != startQuantity -> {}
                }
            } else if (activeOrders.isNotEmpty()) { // no need to buy
                val cancelList = activeOrders.map { it.id }
                api.cancelOrders(cancelList)
            }
        }
    }

    private suspend fun bestPrice(pairName: String, config: BotConfigEntity): Double? {
        val startPrice = config.startPrice
        val biasPrice = config.biasPrice
        val priceForce = config.priceForce
        val ticker = tickersDao.getByPairName(pairName)
        val tickerPrice = ticker?.priceLast
        val book = api.getOrderBook(pairName, 5)
            .onFailure { Log.warn(it) { "Can't get public orders book from Kuna API for Pair = $pairName" } }
            .getOrNull()
        val bookPrice = book?.bids?.minBy { it.price }?.price

        val price = when {
            tickerPrice != null && bookPrice != null && tickerPrice >= bookPrice
                    || tickerPrice == null && bookPrice != null -> {
                when (bookPrice < startPrice) {
                    true -> startPrice
                    false -> bookPrice
                }
            }

            tickerPrice != null && bookPrice != null && tickerPrice < bookPrice
                    || tickerPrice != null && bookPrice == null -> {
                when (tickerPrice < startPrice) {
                    true -> startPrice
                    false -> tickerPrice
                }
            }

            else -> return startPrice
        }

        val bestPrice = when ((priceForce)) {
            true -> price
            false -> price + price * biasPrice / 100
        }

        return bestPrice
    }

    private suspend fun createNewGridOrders(pair: String, config: BotConfigEntity, tickPrice: Double) {
        val biasPrice = config.biasPrice
        val priceForce = config.priceForce
        var priceStart = tickPrice + tickPrice * biasPrice / 100
        if (priceForce) priceStart = tickPrice
        if (priceStart < config.startPrice) priceStart = config.startPrice
        val priceStep = config.priceStep
        val sellQuantity = config.minSize * config.orderSize
        val sizeStep = config.sizeStep

        val requests = (0 until config.orderAmount).map { item ->
            val price = priceStart + priceStart * item * priceStep / 100
            val quantity = sellQuantity + sellQuantity * item * sizeStep / 100
            CreateOrderRequest(
                Order.Type.Limit, Order.Side.Buy, pair, price, quantity
            )
        }

        Log.info {
            "Orders to create on web:\n" + requests.joinToString("\n")
        }

        requests.forEach {
            api.createOrder(it)
                .onFailure { Log.warn(it) { "Order wasn't created" } }
        }
    }

    data object Params

    suspend operator fun invoke() = executeSync(Params)
}