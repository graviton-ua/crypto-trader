package ua.hospes.cryptogateway.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.domain.interactors.CreateOrder
import ua.cryptogateway.domain.interactors.CreateOrder.Params.Side
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class HomeViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
    private val createOrder: CreateOrder,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    fun onClick() {
        viewModelScope.testRun()
    }
    private fun CoroutineScope.testRun() = launch(dispatcher) {


        createOrder.limit(
            orderSide = Side.Sell, pair = "DOGE_USDT", price = "0.61", quantity = "0.01"
        )
        //{"errors":[{"code":"WrongRequestException","message":"price must be a number string"},{"code":"WrongRequestException","message":"quantity must be a number string"}]}
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}