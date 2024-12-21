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
//            70307.19,69562.86,69581.91,68909.37,67882.85,69458.14,75179.21,75779.46,76470.2,76619.12,80152.11,87706.55,
//            87210.14,90150.68,87324.52,90726.99,90119.25,89467.45,90509.91,92457.82,94204.91,97784.65,98355.06,97113.29,97891.72,92846.14,
//            91913.95,95883.19,95690.84,97374.43,96263.32,97026.18,95815.67,95750.94,98581.58,97094.16,99695.25,99728.31,101043.67,97434.39,96912.13,
//            100900.57, 99923.68, 101394.34, 101261.05, 104181.49, 105746.04, 106142.79, 100195.80, 97703.97, 98124.17,
//        )
        val entities = measureTimedValue { dao.getAllClosedForEachMinute("BTC_USDT") }.also { println("Read candles Exec time: ${it.duration}") }.value
        println(entities.joinToString("\n"))

        val prices = entities.map { it.closePrice }

        val result = measureTimedValue { prices.crsi(3, 2, 10) }.also { println("CRSI Exec time: ${it.duration}") }.value
    }
}