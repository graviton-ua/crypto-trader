package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.models.BotConfigEntity
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.requests.CreateOrderRequest
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class PlaceSellLimitOrders(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val configsDao: BotConfigsDao,
    private val balanceDao: BalanceDao,
    private val orderDao: OrderDao,

    private val createOrder: CreateOrder,
) : ResultInteractor<PlaceSellLimitOrders.Params, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Unit = withContext(dispatcher) {
        val configs = configsDao.getActive().filter { it.side == Order.Side.Sell }

        configs.forEach { config ->
            val balance = balanceDao.getCurrency(currency = config.baseAsset) ?: return@forEach

            if (balance.balance < config.minSize) return@forEach

            val pairName = config.pair
            val book = api.getOrderBook(pairName, 5)
                .onFailure { Log.warn(it, TAG) { "Can't get public orders book from Kuna API for Pair = $pairName" } }
                .getOrNull() ?: return@forEach
            val bookPrice = book.bids.minBy { it.price }.price

            val activeOrders = orderDao.get(side = Order.Side.Sell)
                .onFailure { Log.warn(it, TAG) { "Can't get active orders from table" } }
                .getOrNull() ?: return@forEach
            when {
                // We have not enough balance, quit
                balance.balance < config.fond -> {
                    val cancelList = activeOrders.map { it.id }
                    api.cancelOrders(cancelList)
                    createNewGridOrders(pairName, config, bookPrice)
                    // return
                }
                // We have enough balance, can create new orders
                balance.balance >= config.fond -> {
                    // We are checking amount orders
                    val activeOrdersAmount = activeOrders.size
                    when {
                        activeOrdersAmount < config.orderAmount -> {
                            val cancelList = activeOrders.map { it.id }
                            api.cancelOrders(cancelList)
                            createNewGridOrders(pairName, config, bookPrice)
                            // return
                        }

                        activeOrdersAmount > config.orderAmount -> {
                            val cancelAmount = activeOrdersAmount - config.orderAmount
                            val cancelList = activeOrders.map { it.id }.takeLast(cancelAmount)
                            api.cancelOrders(cancelList)
                            // return
                        }

                        else -> {
                            // We are checking price first order

                            val minPrice = activeOrders.minOf { it.price }
                            val priceStart = when (config.priceForce) {
                                true -> bookPrice
                                false -> bookPrice + (bookPrice * config.biasPrice / 100)
                            }

                            val minQuantity = activeOrders.minOf { it.quantity }
                            val sellQuantity = config.minSize * config.orderSize

                            when {
                                minPrice != priceStart -> {
                                    val cancelList = activeOrders.map { it.id }
                                    api.cancelOrders(cancelList)
                                    createNewGridOrders(pairName, config, bookPrice)
                                    // return
                                }

                                // We are checking quantity first order
                                minQuantity != sellQuantity -> {
                                    val cancelList = activeOrders.map { it.id }
                                    api.cancelOrders(cancelList)
                                    createNewGridOrders(pairName, config, bookPrice)
                                    // return
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private suspend fun createNewGridOrders(pair: String, config: BotConfigEntity, bookPrice: Double) {
        val biasPrice = config.biasPrice
        val priceForce = config.priceForce
        var priceStart = bookPrice + bookPrice * biasPrice / 100
        if (priceForce) priceStart = bookPrice
        if (priceStart < config.startPrice) priceStart = config.startPrice
        val priceStep = config.priceStep
        val sellQuantity = config.minSize * config.orderSize
        val sizeStep = config.sizeStep

        val requests = (0 until config.orderAmount).map { item ->
            val price = priceStart + priceStart * item * priceStep / 100
            val quantity = sellQuantity + sellQuantity * item * sizeStep / 100
            CreateOrderRequest(
                Order.Type.Limit, Order.Side.Sell, pair, price, quantity
            )
        }
        Log.info(tag = TAG) {
            "Orders to create on web:\n" + requests.joinToString("\n")
        }

        requests.forEach {
//            api.createOrder(newOrder)
//                .onFailure { Log.warn(it, TAG) { "Order wasn't created" } }
        }
    }

    data object Params

    suspend operator fun invoke() = executeSync(Params)

    companion object {
        private const val TAG = "PlaceSellLimitOrders"
    }
}