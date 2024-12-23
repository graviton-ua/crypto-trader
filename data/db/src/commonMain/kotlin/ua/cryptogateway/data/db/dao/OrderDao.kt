package ua.cryptogateway.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.Database
import ua.cryptogateway.data.db.models.OrderEntity
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OrderDao(
    dispatchers: AppCoroutineDispatchers,
    db: Database,
) : Dao(dispatcher = dispatchers.io, db = db) {

    suspend fun getAll(): List<OrderEntity> = transaction {
        ordersQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun get(side: Order.Side): List<OrderEntity> = transaction {
        ordersQueries.getForSide(side = side, mapper = mapper).executeAsList()
    }

    suspend fun getCancelID() = transaction {
        ordersQueries.getForCancel(mapper = mapper).executeAsList()
    }

    suspend fun deleteById(ids: List<String>) = Result.runCatching {
        transaction {
            ordersQueries.deleteInIds(ids = ids)
        }
    }

    suspend fun save(entity: OrderEntity) = Result.runCatching {
        transaction {
            ordersQueries.save(
                id = entity.id,
                type = entity.type,
                quantity = entity.quantity,
                executedQuantity = entity.executedQuantity,
                cumulativeQuoteQty = entity.cumulativeQuoteQty,
                cost = entity.cost,
                pair = entity.pair,
                side = entity.side,
                price = entity.price,
                status = entity.status,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
            )
        }
    }

    suspend fun saveActive(orders: List<OrderEntity>) = Result.runCatching {
        transaction {
            ordersQueries.deleteNotInIds(ids = orders.map { it.id })

            orders.forEach { entity ->
                ordersQueries.save(
                    id = entity.id,
                    type = entity.type,
                    quantity = entity.quantity,
                    executedQuantity = entity.executedQuantity,
                    cumulativeQuoteQty = entity.cumulativeQuoteQty,
                    cost = entity.cost,
                    pair = entity.pair,
                    side = entity.side,
                    price = entity.price,
                    status = entity.status,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt,
                )
            }
        }
    }
}

private val mapper: (
    String, Order.Type, Double, Double, Double, Double, Order.Side, String, Double, String, Instant, Instant, Boolean,
) -> OrderEntity = ::OrderEntity