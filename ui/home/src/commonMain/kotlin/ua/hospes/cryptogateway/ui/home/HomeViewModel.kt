package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.db.dao.ActiveDao

import ua.cryptogateway.data.db.models.TickerEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    appscope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val daoTick: TickersDao,

    private val daoBal: BalanceDao,
    private val daoAct: ActiveDao,
) : ViewModel() {
    private val dispatcher = dispatchers.io
//    private val tickersPullService = TickersPullService(dispatchers,  appscope, api, daoTick)
//    private val balancePullService = BalancePullService(dispatchers, appscope, api, daoBal)
//    init {
//        // Запускаем процесс получения тикеров
//        tickersPullService.start()
//    }
//    init {
//        // Запускаем процесс получения баланса
//        balancePullService.start()
//    }

//    override fun onCleared() {
//        super.onCleared()
//        tickersPullService.stop() // Останавливаем сервис при уничтожении ViewModel
//    }
//    override fun onCleared() {
//        super.onCleared()
//        tickersPullService.stop() // Останавливаем сервис при уничтожении ViewModel
//    }


    fun onClick() {
        viewModelScope.testRun()
    }

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
        val ticker: TickerEntity? = daoTick.getByPairName(pairName = "ETHW_USDT")
        ticker?.timestamp
//        var listTicker = daoTick.readAll()
//        Log.info(tag = TAG) { "listTicker: $listTicker" }
//        val prices = listTicker.map { tpL -> tpL.priceLast }
//        val priceLast = listTicker.first().priceLast
//        val priceLastGet = listTicker[0].priceLast

        //listTicker = daoTick.getByPairName(pairName = "DOGE_USDT")
        Log.info(tag = TAG) { "Ticker: $ticker" }
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
        var listBal = daoBal.getCurrency(currency = "DOGE")
        Log.info(tag = TAG) { "Balance : $listBal" }
        listBal = daoBal.getCurrency(currency = "ETHW")
        Log.info(tag = TAG) { "Balance : $listBal" }


        val kunaActiveList = api.getActive()
            .onSuccess { value ->
                Log.info(tag = TAG) { "getActive: $value" }
            }
            .onFailure { exception ->
                Log.error(tag = TAG, throwable = exception) { "getActive: Some error happen" }
            }.getOrNull()
        // Example fetching Active from database
        val activeDatabaseList = daoAct.readAll()
        Log.info(tag = TAG) { "getActive: $activeDatabaseList kunaList: $kunaActiveList" }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}