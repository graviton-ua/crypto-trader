package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.models.OrderEntity
import ua.cryptogateway.data.db.models.OrderType
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaOrder
import ua.cryptogateway.data.web.requests.CreateOrderRequest
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.data.models.Side
import ua.cryptogateway.util.AppCoroutineDispatchers

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
                type = params.type.code,
                orderSide = params.side.code,
                pair = params.pair,
                price = params.price.toString(),
                quantity = params.quantity.toString(),
            )

            is Params.Market -> CreateOrderRequest(
                type = params.type.code,
                orderSide = params.side.code,
                pair = params.pair,
                quantity = params.quantity.toString(),
            )
        }

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


    suspend fun limit(
        side: Side,
        pair: String,
        quantity: Double,
        price: Double,
    ) = executeSync(Params.Limit(side, pair, quantity, price))

    suspend fun market(
        side: Side,
        pair: String,
        quantity: Double,
    ) = executeSync(Params.Market(side, pair, quantity))

    sealed interface Params {
        val type: OrderType

        data class Limit(
            val side: Side,
            val pair: String,
            val quantity: Double,
            val price: Double,
        ) : Params {
            override val type: OrderType = OrderType.Limit
        }

        data class Market(
            val side: Side,
            val pair: String,
            val quantity: Double,
        ) : Params {
            override val type: OrderType = OrderType.Market
        }
    }


    companion object {
        private const val TAG = "CreateOrder"
    }
}

internal fun KunaOrder.toEntity(): OrderEntity {
    return OrderEntity(
        id = id,
        type = OrderType.fromKunaString(type),
        quantity = quantity,
        executedQuantity = executedQuantity,
        cumulativeQuoteQty = 0.0,
        cost = 0.0,
        side = Side.fromKunaString(side),
        pair = pair,
        price = price,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}