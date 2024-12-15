package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.domain.interactors.CreateOrder
import ua.cryptogateway.data.models.Side
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val tickersDao: TickersDao,
    private val botConfigsDao: BotConfigsDao,
    private val orderDao: OrderDao,
    private val createOrder: CreateOrder,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    fun onClick() {
        viewModelScope.testRun()
    }

    private fun CoroutineScope.testRun() = launch(dispatcher) {

        createOrder.limit(
            side = Side.Sell, pair = "DOGE_USDT", price = 0.61, quantity = 0.01
        )
            .onSuccess { }
            .onFailure { }
//-------------------------------------------------------------------------------------------------------------------
        val ordersFilter = orderDao.getAll().getOrDefault(emptyList())
            .filter { it.side.code == "Bid" && it.pair == "DOGE_USDT" } // Фильтруем по side и pair
        val countOrders = ordersFilter.size
//        println("countOrders : $countOrders")

        val maxPrice =
            ordersFilter.maxOf { sl -> sl.price }//.maxOfOrNull { order -> order.price } // Находим максимальную цену
        val minPrice =
            ordersFilter.minOf { sl -> sl.price }//.minOfOrNull { order -> order.price } // Находим максимальную цену
//        println("minPrice : $minPrice   maxPrice : $maxPrice")

        val ticker = tickersDao.getAll().filter { it.pairName == "DOGE_USDT" }
        println("DOGE_USDT  tickerBid: ${ticker[0].priceBid}   tickerAsk: ${ticker[0].priceAsk}  change: ${ticker[0].change}")


        val book = api.getOrderBook("DOGE_USDT", 5).getOrNull() ?: return@launch

//        val bookOrder = book.bids.minBy { it.price }
//        println("bookOrder: $bookOrder")
//        println("bookOrder.Price: ${bookOrder.price}")
//        println("bookOrder.Quantity: ${bookOrder.quantity}")
        val bookBID0 = book.bids.get(0)//.minBy { it.price }
        println("bookBID[0]  Price: ${bookBID0.price}   Quantity: ${bookBID0.quantity}")
        val bookASK0 = book.asks.get(0)//.minBy { it.price }
        println("bookASK[0]  Price: ${bookASK0.price}   Quantity: ${bookASK0.quantity}\n")

//-----------------------------------------------------------------------------------------------------------------


        val active = botConfigsDao.getActiveTickers()
//        Log.info(tag = TAG) { "Active tickers: $active" }
        for (pair in active) {
            api.getTradesBook(pair, 1)
//                .onSuccess { value ->
//                    Log.info(tag = TAG) { "getTradesBook: $value" }
//                }
                .onFailure { exception ->
                    Log.error(tag = TAG, throwable = exception) { "getTradesBook: Some error happen" }
                }
        }

    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}