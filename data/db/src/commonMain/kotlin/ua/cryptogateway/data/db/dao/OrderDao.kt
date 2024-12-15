package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notInList
import ua.cryptogateway.data.db.models.OrderEntity
import ua.cryptogateway.data.db.schema.OrderSchema
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OrderDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    suspend fun getAll(): Result<List<OrderEntity>> = Result.runCatching {
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

    suspend fun get(
        side: Order.Side,
    ): Result<List<OrderEntity>> = Result.runCatching {
        dbQuery {
            OrderSchema.selectAll()
                .where { OrderSchema.side eq side }
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

    suspend fun getCancelID() = Result.runCatching {
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
            OrderSchema.upsert(
                onUpdateExclude = listOf(OrderSchema.cancel),
            ) {
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
            }
        }
    }

    suspend fun saveActive(orders: List<OrderEntity>) = dbQuery {
        OrderSchema.deleteWhere { OrderSchema.id notInList orders.map { it.id } }

        OrderSchema.batchUpsert(
            data = orders,
            onUpdateExclude = listOf(OrderSchema.cancel),
        ) {
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
        }
    }
}