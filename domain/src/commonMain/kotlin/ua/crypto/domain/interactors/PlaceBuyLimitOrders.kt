package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.BalanceDao
import ua.crypto.data.db.dao.BotConfigsDao
import ua.crypto.data.db.dao.OrderDao
import ua.crypto.data.db.dao.TickersDao
import ua.crypto.data.db.models.BotConfigEntity
import ua.crypto.data.models.Order
import ua.crypto.data.web.api.KunaApi
import ua.crypto.data.web.requests.CreateOrderRequest
import ua.crypto.domain.ResultInteractor

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

            val quotAsset = balanceDao.getForAsset(asset = config.quoteAsset) ?: return@forEach

            val pairName = config.baseAsset + "_" + config.quoteAsset
            val startQuantity = config.minSize * config.orderSize
            val bestPrice = bestPrice(pairName, config) ?: return@forEach

            val quotAssetLimit = quotAsset.balance
            // If there is not enough quoteAsset even to purchase the minimum volume
            if (quotAssetLimit < (startQuantity * bestPrice)) {
                val formatBalance = String.format("%.6f", quotAssetLimit)
                val formatRequired = String.format("%.6f", startQuantity * bestPrice)
                Log.info {
                    "Balance < minimum Order Quantity!  Balance= $formatBalance ${config.quoteAsset}; " +
                            "Coins required=$formatRequired ${config.quoteAsset}"
                }
                return@forEach
            }

            val balance = balanceDao.getForAsset(asset = config.baseAsset) ?: return@forEach

            val activeOrders = orderDao.get(side = Order.Side.Buy)
            val buyLimit = config.fond - balance.balance
            if (buyLimit > 0) {    // need to buy
            if (activeOrders.isEmpty()) {
                Log.info { "Active Orders table is Empty!" }
                createNewGridOrders(pairName, config, bestPrice, buyLimit, quotAssetLimit)
            } else {
                val activeOrdersAmount = activeOrders.size

                when {
                    activeOrdersAmount < config.orderAmount -> {
                        val cancelList = activeOrders.map { it.id }
                        Log.info { "Canceled all orders. Active amount < Config amount." }
                        api.cancelOrders(cancelList)
                        createNewGridOrders(pairName, config, bestPrice, buyLimit, quotAssetLimit)
                        // return
                    }

                    activeOrdersAmount > config.orderAmount -> {
                        val cancelAmount = activeOrdersAmount - config.orderAmount
                        val cancelList = activeOrders.map { it.id }.takeLast(cancelAmount)
                        Log.info { "Canceled only a few orders: $cancelList" }
                        api.cancelOrders(cancelList)
                        // return
                    }

                    activeOrders.minOf { it.price } != bestPrice -> {
                        val cancelList = activeOrders.map { it.id }
                        Log.info { "Canceled all orders. Min Price != Best Price." }
                        api.cancelOrders(cancelList)
                        createNewGridOrders(pairName, config, bestPrice, buyLimit, quotAssetLimit)
                        // return
                    }

                    activeOrders.minOf { it.quantity } != startQuantity -> {
                        val cancelList = activeOrders.map { it.id }
                        Log.info { "Canceled all orders. Min Quantity != Start Quantity." }
                        api.cancelOrders(cancelList)
                        createNewGridOrders(pairName, config, bestPrice, buyLimit, quotAssetLimit)
                        // return
                    }
                }
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
                when (bookPrice > startPrice) {
                    true -> startPrice
                    false -> bookPrice
                }
            }

            tickerPrice != null && bookPrice != null && tickerPrice < bookPrice
                    || tickerPrice != null && bookPrice == null -> {
                when (tickerPrice > startPrice) {
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
        Log.info { "Best=$bestPrice; Ticker=$tickerPrice; Book=$bookPrice; Start=$startPrice; Bias=$biasPrice; Force=$priceForce" }
        return bestPrice
    }

    private suspend fun createNewGridOrders(
        pair: String,
        config: BotConfigEntity,
        bestPrice: Double,
        buyLimit: Double,
        quotAssetLimit: Double
    ) {
        val priceStep = config.priceStep
        val sellQuantity = config.minSize * config.orderSize
        val sizeStep = config.sizeStep
        var sumQuantity = 0.0
        var balanceLimit = quotAssetLimit

        val requests = (0 until config.orderAmount).map { item ->
            val price = bestPrice - bestPrice * item * priceStep / 100
            val quantity = sellQuantity + sellQuantity * item * sizeStep / 100
            sumQuantity += quantity
            balanceLimit -= (price * quantity)
            if (sumQuantity >= buyLimit || balanceLimit < 0) {
                if (balanceLimit < 0)  Log.info { "Order N$item not create! Balance limit exceeded." }
                else  Log.info { "Order N$item not create! Buy limit  exceeded." }
//                Log.info(tag = TAG) {
//                    "Order N$item not create! Price=$formatPrice; Quantity=$formatQuantity.\n " +
//                            "Summa quantity: ${String.format("%.6f", sumQuantity)}  Buy limit: ${String.format("%.6f", buyLimit)}, " +
//                            "Balance limit: ${String.format("%.6f", balanceLimit)}"
//                }
            } else {
                CreateOrderRequest(
                    Order.Type.Limit, Order.Side.Buy, pair, price, quantity
                )
            }
        }
//        Log.info(tag = TAG) {
//            "Summa quantity: ${String.format("%.6f", sumQuantity)}  Buy limit: ${String.format("%.6f", buyLimit)}, " +
//                    "Balance limit: ${String.format("%.6f", balanceLimit)}"
//        }
        Log.info {
            "Orders to create on web:\n" + requests.joinToString("\n")
        }

//        requests.forEach {
//            api.createOrder(it)
//                .onFailure { Log.warn(it, TAG) { "Order wasn't created" } }
//        }
    }

    data object Params

    suspend operator fun invoke() = executeSync(Params)
}