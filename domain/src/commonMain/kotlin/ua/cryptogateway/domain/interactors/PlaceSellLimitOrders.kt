package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.models.BotConfigEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.requests.CreateOrderRequest
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class PlaceSellLimitOrders(
    dispatchers: AppCoroutineDispatchers,
    val api: KunaApi,
    val configsDao: BotConfigsDao,
    val balanceDao: BalanceDao,
    val orderDao: OrderDao,
) : ResultInteractor<PlaceSellLimitOrders.Params, Result<Unit>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Result<Unit> = withContext(dispatcher) {
        val configs = configsDao.getActive().filter { it.side == Order.Side.Sell }

        configs.forEach { config ->
            val balance = balanceDao.getCurrency(currency = config.baseAsset)
                .onFailure { Log.warn(it) { "Can't get balance from table for Currency = ${config.baseAsset}" } }
                .getOrNull() ?: return@forEach

            if (balance.balance < config.minSize) return@forEach

            val pairName = config.baseAsset + "_" + config.quoteAsset
            val book = api.getOrderBook(pairName, 1)
                .onFailure { Log.warn(it) { "Can't get public orders book from Kuna API for Pair = $pairName" } }
                .getOrNull() ?: return@forEach
            val bookPrice = book.bids.minBy { it.price }.price

            val activeOrders = orderDao.get(side = Order.Side.Sell)
                .onFailure { Log.warn(it) { "Can't get active orders from table" } }
                .getOrNull() ?: return@forEach
            when {
                // We have not enough balance, quit
                balance.balance <= config.fond -> {
                    val cancelList = activeOrders.map { it.id }
                    api.cancelOrders(cancelList)
                    // return
                }
                // We have enough balance, can create new orders
                balance.balance > config.fond -> {
                    // We are checking amount orders
                    val activeOrdersAmount = activeOrders.count()
                    when {
                        activeOrdersAmount < config.orderAmount -> {
                            val cancelList = activeOrders.map { it.id }
                            api.cancelOrders(cancelList)
                            createNewGridOrders(pairName, config, bookPrice)
                            // return
                        }

                        activeOrdersAmount > config.orderAmount -> {
                            val cancelAmount = activeOrdersAmount - config.orderAmount
                            val cancelList = activeOrders.map { it.id }.dropLast(cancelAmount)
                            api.cancelOrders(cancelList)
                            // return
                        }

                        else -> {
                            // We are checking price first order

                            val minPrice = activeOrders.minOf { it.price }
                            var priceStart = bookPrice + bookPrice * config.biasPrice / 100
                            if (config.priceForce) priceStart = bookPrice
                            if (minPrice != priceStart) {
                                val cancelList = activeOrders.map { it.id }
                                api.cancelOrders(cancelList)
                                createNewGridOrders(pairName, config, bookPrice)
                                // return
                            }

                            // We are checking quantity first order
                            val minQuantity = activeOrders.minOf { it.quantity }
                            val sellQuantity = config.minSize * config.orderSize
                            if (minQuantity != sellQuantity) {
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

        Result.success(Unit)
    }

    fun createNewGridOrders(pair: String, config: BotConfigEntity, bookPrice: Double) {
        val biasPrice = config.biasPrice
        val priceForce = config.priceForce
        var priceStart = bookPrice + bookPrice * biasPrice / 100
        if (priceForce) priceStart = bookPrice
        if (priceStart < config.startPrice) priceStart = config.startPrice
        val priceStep = config.priceStep
        val sellQuantity = config.minSize * config.orderSize
        val sizeStep = config.sizeStep
        var sellAmount: Int = config.orderAmount
        var item = 0
        while (sellAmount > 0) {
            val price = priceStart + priceStart * item * priceStep / 100
            val quantity = sellQuantity + sellQuantity * item * sizeStep / 100
            val newOrder = CreateOrderRequest(
                "Limit", "Sell", pair, price.toString(), quantity.toString()
            )
            api.createOrder(newOrder)
            item += 1
            sellAmount -= 1
        }

    }

    data object Params

    companion object {
        private const val TAG = "PlaceSellLimitOrders"
    }
}