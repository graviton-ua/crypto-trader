package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.requests.CreateOrderRequest
import ua.cryptogateway.domain.interactors.CreateOrder
import ua.cryptogateway.domain.services.ActiveOrdersPullService
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.uuid.ExperimentalUuidApi

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val daoTick: TickersDao,
    private val daoBal: BalanceDao,
    private val daoAct: OrderDao,
    private val createOrder: CreateOrder,

    private val activeOrdersPullService: ActiveOrdersPullService,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    init {
        // Запускаем процесс получения Активных ордеров
        activeOrdersPullService.start()
    }


    override fun onCleared() {
        super.onCleared()
        activeOrdersPullService.stop() // Останавливаем сервис при уничтожении ViewModel
    }

    fun onClick() {
        viewModelScope.testRun()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun CoroutineScope.testRun() = launch(dispatcher) {

        // Example to do API call
        api.getTickers("ETHW_USDT")
            .onSuccess { value ->
                Log.info(tag = TAG) { "getTickers: $value" }
            }
            .onFailure { exception ->
                Log.error(tag = TAG, throwable = exception) { "getTickers: Some error happen" }
            }

        // Example fetching Tickers from database
//        val ticker: TickerEntity? = daoTick.getByPairName(pairName = "ETHW_USDT")
//        ticker?.timestamp
//        val listTicker = daoTick.getAll()
//        Log.info(tag = TAG) { "listTicker: $listTicker" }
//        val prices = listTicker.map { tpL -> tpL.priceLast }
//        val priceLast = listTicker.first().priceLast
//        val priceLastGet = listTicker[0].priceLast

        //listTicker = daoTick.getByPairName(pairName = "DOGE_USDT")
//        Log.info(tag = TAG) { "Ticker: $ticker" }
//        println(listTicker.get(0))
//        println(listTicker.get(1))


//        api.getBalance()
//            .onSuccess { value ->
//                Log.info(tag = TAG) {"getBalance: $value"}
//            }
//            .onFailure { exception ->
//                Log.error(tag = TAG, throwable = exception) {"getBalance: Error getting balance"}
//            }

        // Example fetching Balance from database
//        var listBal = daoBal.getCurrency(currency = "DOGE")
//        Log.info(tag = TAG) { "Balance : $listBal" }
//        listBal = daoBal.getCurrency(currency = "ETHW")
//        Log.info(tag = TAG) { "Balance : $listBal" }

        val kunaActiveIdSet = api.getActiveOrders()
            .onSuccess { value ->
                Log.info(tag = TAG) { "getActive: $value" }
            }
            .onFailure { exception ->
                Log.error(tag = TAG, throwable = exception) { "getActive: Some error happen" }
            }

        // Example fetching Active from database
        val listActiveAllID = daoAct.readAll().getOrNull()?.map { it.id }
//        Log.info(tag = TAG) { "listActiveAllID : $listActiveAllID" }
        val listCancelID = daoAct.readCancelID().getOrDefault(emptyList())
//        Log.info(tag = TAG) { "listCancelID : $listCancelID" }

        val listOfCancelledOrders = api.cancelOrders(ordersId = listCancelID)
            .onSuccess { Log.info(tag = TAG) { "cancelled orders : $it" } }
            .onFailure { Log.error(tag = TAG, throwable = it) { "cancelOrders failed" } }
            .getOrNull() ?: return@launch

        val listOfSuccessFullyCancelled = listOfCancelledOrders.filter { it.success }
        Log.info(tag = TAG) { "successfully cancelled orders : $listOfSuccessFullyCancelled" }

        daoAct.deleteById(listOfSuccessFullyCancelled.map { it.id })

//        val newOrderRequest = CreateOrderRequest(
//            type = "Limit", orderSide = "Ask", pair = "DOGE_USDT", price = 0.6.toString(), quantity = 1000.01.toString()
//        )
//        println("createOrderRequest: $newOrderRequest")
//
//        val listOfNewOrders = api.createOrder(request = newOrderRequest)
//            .onSuccess {
//                Log.info(tag = TAG) { "new order : $it" }
//            }
//            .onFailure { Log.error(tag = TAG, throwable = it) { "new order failed" } }
//            .getOrNull() ?: return@launch
//        println("listOfNewOrders: $listOfNewOrders")

        createOrder(
            type = CreateOrder.Params.Type.Limit,
            orderSide = "Ask", pair = "DOGE_USDT", price = 0.6, quantity = 0.01,
        )

    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}