package ua.crypto.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.models.OrderEntity
import ua.crypto.data.models.Order
import ua.crypto.data.sql.Database

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