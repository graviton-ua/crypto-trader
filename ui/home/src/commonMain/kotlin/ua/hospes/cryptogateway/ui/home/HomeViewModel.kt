package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.ActiveDao
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.domain.services.ActivePullService
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.uuid.ExperimentalUuidApi

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val daoTick: TickersDao,
    private val daoBal: BalanceDao,
    private val daoAct: ActiveDao,

    private val activePullService: ActivePullService,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    init {
        // Запускаем процесс получения Активных ордеров
        activePullService.start()
    }


    override fun onCleared() {
        super.onCleared()
        activePullService.stop() // Останавливаем сервис при уничтожении ViewModel
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

        val kunaActiveIdSet = api.getActive()
            .onSuccess { value ->
                Log.info(tag = TAG) { "getActive: $value" }
            }
            .onFailure { exception ->
                Log.error(tag = TAG, throwable = exception) { "getActive: Some error happen" }
            }

        // Example fetching Active from database
        val listActiveAllID = daoAct.readAll().getOrNull()?.map { it.id }
        Log.info(tag = TAG) { "listActiveAllID : $listActiveAllID" }
        val listCancelID = daoAct.readCancelID().getOrDefault(emptyList())
        Log.info(tag = TAG) { "listCancelID : $listCancelID" }



        Log.info(tag = TAG) { "listCancelID : $listCancelID" }
        val listOfCancelledOrders = api.cancelOrders(ordersId = listCancelID)
            .onSuccess { Log.info(tag = TAG) { "cancelled orders : $it" } }
            .onFailure { Log.error(tag = TAG, throwable = it) { "cancelOrders failed" } }
            .getOrNull() ?: return@launch

        val listOfSuccessFullyCancelled = listOfCancelledOrders.filter { it.success }
        Log.info(tag = TAG) { "successfully cancelled orders : $listOfSuccessFullyCancelled" }

        //daoAct.deleteActiveById(listOfSuccessFullyCancelled.map { it.id })
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}