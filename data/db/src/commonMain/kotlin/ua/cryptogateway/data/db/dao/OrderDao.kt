package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import ua.cryptogateway.data.db.models.OrderEntity
import ua.cryptogateway.data.db.schema.OrderSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OrderDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    suspend fun readAll(): Result<List<OrderEntity>> = Result.runCatching {
        dbQuery {
            OrderSchema.selectAll()
                .map { row ->
                    OrderEntity(
                        id = row[OrderSchema.id],
                        type = row[OrderSchema.type],
                        quantity = row[OrderSchema.quantity],
                        executedQuantity = row[OrderSchema.executedQuantity],
                        cumulativeQuoteQty = row[OrderSchema.cumulativeQuoteQty],
                        cost = row[OrderSchema.cost],
                        side = row[OrderSchema.side],
                        pair = row[OrderSchema.pair],
                        price = row[OrderSchema.price],
                        status = row[OrderSchema.status],
                        createdAt = row[OrderSchema.createdAt],
                        updatedAt = row[OrderSchema.updatedAt],
                        cancel = row[OrderSchema.cancel]
                    )
                }
        }
    }

    suspend fun readCancelID() = Result.runCatching {
        dbQuery {
            OrderSchema.selectAll()
                .where { OrderSchema.cancel eq true }
                .map { row -> row[OrderSchema.id].trim() }
        }
    }

    suspend fun deleteById(ids: List<String>) = Result.runCatching {
        dbQuery {
            OrderSchema.deleteWhere { id inList ids }
        }
    }

    suspend fun save(entity: OrderEntity) = Result.runCatching {
        dbQuery {
            OrderSchema.upsert {
                it[OrderSchema.id] = entity.id
                it[OrderSchema.type] = entity.type
                it[OrderSchema.quantity] = entity.quantity
                it[OrderSchema.executedQuantity] = entity.executedQuantity
                it[OrderSchema.cumulativeQuoteQty] = entity.cumulativeQuoteQty
                it[OrderSchema.cost] = entity.cost
                it[OrderSchema.side] = entity.side
                it[OrderSchema.pair] = entity.pair
                it[OrderSchema.price] = entity.price
                it[OrderSchema.status] = entity.status
                it[OrderSchema.createdAt] = entity.createdAt
                it[OrderSchema.updatedAt] = entity.updatedAt
                it[OrderSchema.cancel] = entity.cancel
            }
        }
    }

    suspend fun save(orders: List<OrderEntity>) = dbQuery {
        // before we save our new active orders list we need to mark all current orders
        // with possibly cancelled orders, and new list should overwrite with false flags (to not cancel them)
        OrderSchema.update {
            it[OrderSchema.cancel] = true
        }

        OrderSchema.batchUpsert(orders) {
            this[OrderSchema.id] = it.id
            this[OrderSchema.type] = it.type
            this[OrderSchema.quantity] = it.quantity
            this[OrderSchema.executedQuantity] = it.executedQuantity
            this[OrderSchema.cumulativeQuoteQty] = it.cumulativeQuoteQty
            this[OrderSchema.cost] = it.cost
            this[OrderSchema.side] = it.side
            this[OrderSchema.pair] = it.pair
            this[OrderSchema.price] = it.price
            this[OrderSchema.status] = it.status
            this[OrderSchema.createdAt] = it.createdAt
            this[OrderSchema.updatedAt] = it.updatedAt
            this[OrderSchema.cancel] = it.cancel
        }
    }
}