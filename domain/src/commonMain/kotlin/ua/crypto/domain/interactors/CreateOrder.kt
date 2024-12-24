package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.OrderDao
import ua.crypto.data.db.models.OrderEntity
import ua.crypto.data.models.Order
import ua.crypto.data.web.api.KunaApi
import ua.crypto.data.web.models.KunaOrder
import ua.crypto.data.web.requests.CreateOrderRequest
import ua.crypto.domain.ResultInteractor

@Inject
class CreateOrder(
    dispatchers: AppCoroutineDispatchers,
    val api: KunaApi,
    val dao: OrderDao,
) : ResultInteractor<CreateOrder.Params, Result<Unit>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Result<Unit> = withContext(dispatcher) {
        //if (params.quantity > 1) return@withContext Result.failure(IllegalArgumentException("Too big quantity"))

        val newOrderRequest = when (params) {
            is Params.Limit -> CreateOrderRequest(
                type = params.type,
                orderSide = params.side,
                pair = params.pair,
                price = params.price,
                quantity = params.quantity,
            )

            is Params.Market -> CreateOrderRequest(
                type = params.type,
                orderSide = params.side,
                pair = params.pair,
                quantity = params.quantity,
            )
        }

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


    suspend fun limit(
        side: Order.Side,
        pair: String,
        quantity: Double,
        price: Double,
    ) = executeSync(Params.Limit(side, pair, quantity, price))

    suspend fun market(
        side: Order.Side,
        pair: String,
        quantity: Double,
    ) = executeSync(Params.Market(side, pair, quantity))

    sealed interface Params {
        val type: Order.Type

        data class Limit(
            val side: Order.Side,
            val pair: String,
            val quantity: Double,
            val price: Double,
        ) : Params {
            override val type: Order.Type = Order.Type.Limit
        }

        data class Market(
            val side: Order.Side,
            val pair: String,
            val quantity: Double,
        ) : Params {
            override val type: Order.Type = Order.Type.Market
        }
    }
}

internal fun KunaOrder.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        type = type,
        quantity = quantity,
        executedQuantity = executedQuantity,
        cumulativeQuoteQty = 0.0,
        cost = 0.0,
        side = side,
        pair = pair,
        price = price,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}