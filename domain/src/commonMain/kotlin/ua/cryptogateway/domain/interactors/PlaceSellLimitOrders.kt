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
class PlaceSellLimitOrders(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val configsDao: BotConfigsDao,
    private val balanceDao: BalanceDao,
    private val orderDao: OrderDao,
    private val tickersDao: TickersDao
) : ResultInteractor<PlaceSellLimitOrders.Params, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Unit = withContext(dispatcher) {
        val configs = configsDao.getActive().filter { it.side == Order.Side.Sell }

        configs.forEach { config ->
            if (config.orderAmount == 0 || config.orderSize == 0) return@forEach

            val pairName = config.baseAsset + "_" + config.quoteAsset
            val startQuantity = config.minSize * config.orderSize
            val bestPrice = bestPrice(pairName, config) ?: return@forEach

            val balance = balanceDao.getCurrency(currency = config.baseAsset) ?: return@forEach

            // If there is no balance to purchase the minimum quantity
            if (balance.balance < config.minSize) return@forEach

            val activeOrders = orderDao.get(side = Order.Side.Sell)
                .onFailure { Log.warn(it) { "Can't get active orders from table" } }
                .getOrNull() ?: return@forEach

            if (activeOrders.isEmpty()) {
                Log.info { "Active Orders table is Empty!" }
                if (balance.balance > config.fond) {
                    createNewGridOrders(pairName, config, bestPrice, balance.balance - config.fond)
                }
            } else {
                val activeQuantity = activeOrders.sumOf { it.quantity } // а если список пуст??????
                val activeOrdersAmount = activeOrders.size

                when {
                    balance.balance < config.fond -> {
                        val cancelList = activeOrders.map { it.id }
                        Log.info { "Canceled all orders. Balance < Fond." }
                        api.cancelOrders(cancelList)
                        // We return the amount of coins of deleted orders to the balance
                        val maxVolume = balance.balance - config.fond + activeQuantity
                        createNewGridOrders(pairName, config, bestPrice, maxVolume)
                        // return
                    }

                    activeOrdersAmount < config.orderAmount -> {
                        val cancelList = activeOrders.map { it.id }
                        Log.info { "Canceled all orders. Active amount < Config amount." }
                        api.cancelOrders(cancelList)
                        // We return the amount of coins of deleted orders to the balance
                        val maxVolume = balance.balance - config.fond + activeQuantity
                        createNewGridOrders(pairName, config, bestPrice, maxVolume)
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
                        // We return the amount of coins of deleted orders to the balance
                        val maxVolume = balance.balance - config.fond + activeQuantity
                        createNewGridOrders(pairName, config, bestPrice, maxVolume)
                        // return
                    }

                    // We are checking quantity first order
                    activeOrders.minOf { it.quantity } != startQuantity -> {
                        val cancelList = activeOrders.map { it.id }
                        Log.info { "Canceled all orders. Min Quantity != Start Quantity." }
                        api.cancelOrders(cancelList)
                        // We return the amount of coins of deleted orders to the balance
                        val maxVolume = balance.balance - config.fond + activeQuantity
                        createNewGridOrders(pairName, config, bestPrice, maxVolume)
                        // return
                    }
                }
            }


//            when {
//            activeOrders.isEmpty() -> {
//
//                // return
//            }
//            // We have not enough balance, quit
//
//            // We have enough balance, can create new orders
//            balance.balance >= config.fond -> {
//                // We are checking amount orders
//                when {
//
//
//                    else -> {
//                        // We are checking price first order
//
//                        val minPrice = activeOrders.minOf { it.price }
//                        val priceStart = when (config.priceForce) {
//                            true -> bestPrice
//                            false -> bestPrice + (bestPrice * config.biasPrice / 100)
//                        }
//
//                        val minQuantity = activeOrders.minOf { it.quantity }
//                        val sellQuantity = config.minSize * config.orderSize
//
//                        when {
//
//                        }
//                    }
//                }
//
//            }
//        }
        }
    }

    private suspend fun bestPrice(pairName: String, config: BotConfigEntity): Double? {
        val startPrice = config.startPrice
        val biasPrice = config.biasPrice
        val priceForce = config.priceForce
        val ticker = tickersDao.getByPairName(pairName)
        val tickerPrice = ticker?.priceLast
        val book = api.getOrderBook(pairName, 5)
            .onFailure {
                Log.warn { "Can't get public orders book from Kuna API for Pair = $pairName" }
            }
            .getOrNull()
        val bookPrice = book?.bids?.minBy { it.price }?.price
        val price = when {
            tickerPrice != null && bookPrice != null && tickerPrice <= bookPrice
                    || tickerPrice == null && bookPrice != null -> {
                when (bookPrice < startPrice) {
                    true -> startPrice
                    false -> bookPrice
                }
            }

            tickerPrice != null && bookPrice != null && tickerPrice > bookPrice
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
        Log.info { "Best=$bestPrice; Ticker=$tickerPrice; Book=$bookPrice; Star=$startPrice; Bias=$biasPrice; Force=$priceForce" }
        return bestPrice
    }

    private suspend fun createNewGridOrders(
        pair: String,
        config: BotConfigEntity,
        bestPrice: Double,
        maxVolume: Double
    ) {

        val priceStep = config.priceStep
        val sellQuantity = config.minSize * config.orderSize
        val sizeStep = config.sizeStep
        var totalVolume = 0.0

        val requests = (0 until config.orderAmount).map { item ->
            val price = bestPrice + bestPrice * item * priceStep / 100
            val quantity = sellQuantity + sellQuantity * item * sizeStep / 100
            totalVolume += quantity
            if (totalVolume >= maxVolume) {
                val formatPrice = String.format("%.6f", price)
                val formatQuantity = String.format("%.6f", quantity)
                val formatTotVolume = String.format("%.6f", totalVolume)
                val formatMaxVolume = String.format("%.6f", maxVolume)
                Log.info { "Order not create! Max Volume=$formatMaxVolume" }
//                Log.info(tag = TAG) {
//                    "Order not create! Price=$formatPrice; Quantity=$formatQuantity.\n " +
//                            "Maximum volume exceeded! Total Volume: $formatTotVolume  Max Volume: $formatMaxVolume"
//                }
            } else {
                CreateOrderRequest(
                    Order.Type.Limit, Order.Side.Sell, pair, price, quantity
                )
            }
        }
        Log.info {
            "Orders to create on web:\n" + requests.joinToString("\n")
        }
        requests.forEach {
//            api.createOrder(newOrder)
//                .onFailure { Log.warn(it) { "Order wasn't created" } }
        }
    }

    data object Params

    suspend operator fun invoke() = executeSync(Params)
}