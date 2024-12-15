package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.interactors.PlaceBuyLimitOrders
import ua.cryptogateway.domain.interactors.PlaceSellLimitOrders
import ua.cryptogateway.util.AppCoroutineDispatchers

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
        placeSellLimitOrders()
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}