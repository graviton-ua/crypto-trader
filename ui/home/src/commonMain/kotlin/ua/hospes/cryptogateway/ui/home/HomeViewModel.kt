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

        createOrder.limit(
            orderSide = Side.Ask, pair = "DOGE_USDT", price = "0.6", quantity = "0.01"
        )
    //{"errors":[{"code":"WrongRequestException","message":"price must be a number string"},{"code":"WrongRequestException","message":"quantity must be a number string"}]}
//        val tt = api.getOrderBook("DOGE_USDT", level = 5)
//            .onSuccess { Log.info(tag = TAG) { "Book: $it" } }
//            .onFailure { Log.error(tag = TAG, throwable = it) }
    }


    companion object {
        private const val TAG = "HomeViewModel"
    }
}