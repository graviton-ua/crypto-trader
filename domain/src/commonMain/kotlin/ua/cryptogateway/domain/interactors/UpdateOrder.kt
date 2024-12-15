package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.models.Side
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.requests.CreateOrderRequest
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

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
            type = params.type.code,
            orderSide = params.orderSide,
            pair = params.pair,
            price = params.price,
            quantity = params.quantity,
        )

        val newOrder = api.createOrder(request = newOrderRequest)
            .onSuccess {
                Log.info(tag = TAG) { "New order created: $it" }
            }
            .onFailure {
                Log.error(tag = TAG, throwable = it) { "Creation of new order failed" }
            }
            .getOrNull() ?: return@withContext Result.failure(IllegalStateException("We didn't get order object"))

        return@withContext dao.save(newOrder.toEntity())
            .onSuccess {
                Log.info(tag = TAG) { "New order saved to database: $it" }
            }
            .onFailure {
                Log.error(tag = TAG, throwable = it) { "New order wasn't saved" }
            }
            .map { }
    }


    suspend operator fun invoke(
        type: Params.Type,
        orderSide: Side,
        pair: String,
        price: String,
        quantity: String,
    ) = executeSync(Params(type, orderSide, pair, price, quantity))


    data class Params(
        val type: Type,
        val orderSide: Side,
        val pair: String,
        val price: String,
        val quantity: String,
    ) {
        enum class Type(val code: String) {
            Limit("Limit"),
            Market("Market"),
            StopLossLimit("StopLossLimit"),
            TakeProfitLimit("TakeProfitLimit"),
        }
    }


    companion object {
        private const val TAG = "CreateOrder"
    }
}