package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.connorsrsi.rsi
import ua.cryptogateway.domain.interactors.PlaceBuyLimitOrders
import ua.cryptogateway.domain.interactors.PlaceSellLimitOrders
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.measureTimedValue

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val placeBuyLimitOrders: PlaceBuyLimitOrders,
    private val placeSellLimitOrders: PlaceSellLimitOrders,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    fun onClick() {
        viewModelScope.testRun()
    }

    private fun CoroutineScope.testRun() = launch(dispatcher) {
        //placeSellLimitOrders()
        val prices: List<Double> = listOf(
            80365.0, 88566.0, 87932.0, 90453.0, 87319.0, 91023.0, 90554.0, 89843.0, 90535.0, 92247.0,
            94243.0, 98305.0, 98688.0, 97538.0, 97828.0, 93160.0, 92101.0, 95953.0, 95761.0, 97579.0,
            96505.0, 97365.0, 95912.0, 96044.0, 98697.0, 96856.0, 99789.0, 99936.0, 101240.0, 97410.0,
            96650.0, 101090.0, 100010.0, 101370.0, 101320.0, 104340.0, 105870.0, 106020.0, 100110.0,
            97482.0,
        )
        val rsiResult = measureTimedValue { prices.rsi(3) }.also { Log.info(tag = TAG) { "Exec time: ${it.duration}" } }.value

        for (i in rsiResult.indices) {
            println("${"%10.2f".format(prices[i])} | ${"%10.2f".format(rsiResult[i])}")
        }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}