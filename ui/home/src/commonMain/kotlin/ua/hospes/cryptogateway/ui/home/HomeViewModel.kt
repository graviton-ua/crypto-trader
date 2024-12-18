package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.connorsrsi.crsi
import ua.cryptogateway.data.db.dao.OhlcvDao
import ua.cryptogateway.domain.interactors.PlaceBuyLimitOrders
import ua.cryptogateway.domain.interactors.PlaceSellLimitOrders
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.measureTimedValue

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val placeBuyLimitOrders: PlaceBuyLimitOrders,
    private val placeSellLimitOrders: PlaceSellLimitOrders,
    private val dao: OhlcvDao,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    fun onClick() {
        viewModelScope.testRun()
    }

    private fun CoroutineScope.testRun() = launch(dispatcher) {
        //placeBuyLimitOrders()
        //placeSellLimitOrders()
//        val prices: List<Double> = listOf(
//            80365.0, 88566.0, 87932.0, 90453.0, 87319.0, 91023.0, 90554.0, 89843.0, 90535.0, 92247.0,
//            94243.0, 98305.0, 98688.0, 97538.0, 97828.0, 93160.0, 92101.0, 95953.0, 95761.0, 97579.0,
//            96505.0, 97365.0, 95912.0, 96044.0, 98697.0, 96856.0, 99789.0, 99936.0, 101240.0, 97434.0,
//            96912.0, 100900.0, 99923.0, 101394.0, 101261.0, 104181.0, 105746.0, 106142.0, 100189.02,
//            97703.97,
//        )
        val entities = measureTimedValue { dao.getAllClosedForEachMinute("BTC_USDT") }.also { println("Read candles Exec time: ${it.duration}") }.value
        println(entities.joinToString("\n"))

        val prices = entities.map { it.closePrice }

        val result = measureTimedValue { prices.crsi(3, 2, 10) }.also { println("CRSI Exec time: ${it.duration}") }.value
    }
}