package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.OrderDao
import ua.crypto.data.models.Order
import ua.crypto.data.web.api.KunaApi
import ua.crypto.data.web.requests.CreateOrderRequest
import ua.crypto.domain.ResultInteractor

@Inject
class UpdateOrder(
    dispatchers: AppCoroutineDispatchers,
    val api: KunaApi,
    val dao: OrderDao,
) : ResultInteractor<UpdateOrder.Params, Result<Unit>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Result<Unit> = withContext(dispatcher) {
        //if (params.quantity > 1) return@withContext Result.failure(IllegalArgumentException("Too big quantity"))

//        val newOrderRequest = CreateOrderRequest(
//            type = "Limit", orderSide = "Ask", pair = "DOGE_USDT", price = 0.6, quantity = 0.01
//        )
        val newOrderRequest = CreateOrderRequest(
            type = params.type,
            orderSide = params.orderSide,
            pair = params.pair,
            price = params.price,
            quantity = params.quantity,
        )

        val newOrder = api.createOrder(request = newOrderRequest)
            .onSuccess {
                Log.info { "New order created: $it" }
            }
            .onFailure {
                Log.error(throwable = it) { "Creation of new order failed" }
            }
            .getOrNull() ?: return@withContext Result.failure(IllegalStateException("We didn't get order object"))

        return@withContext dao.save(newOrder.toEntity())
            .onSuccess {
                Log.info { "New order saved to database: $it" }
            }
            .onFailure {
                Log.error(throwable = it) { "New order wasn't saved" }
            }
            .map { }
    }


    suspend operator fun invoke(
        type: Order.Type,
        orderSide: Order.Side,
        pair: String,
        price: String,
        quantity: String,
    ) = executeSync(Params(type, orderSide, pair, price, quantity))


    data class Params(
        val type: Order.Type,
        val orderSide: Order.Side,
        val pair: String,
        val price: String,
        val quantity: String,
    )
}