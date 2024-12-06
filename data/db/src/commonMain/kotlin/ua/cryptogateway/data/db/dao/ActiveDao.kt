package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.selectAll
import ua.cryptogateway.data.db.models.ActiveEntity
import ua.cryptogateway.data.db.schema.ActiveSchema
import ua.cryptogateway.data.db.schema.KunaListSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ActiveDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    suspend fun readAll(): Result<List<ActiveEntity>> = Result.runCatching {
        dbQuery {
            ActiveSchema.selectAll()
                .map { row ->
                    ActiveEntity(
                        id = row[ActiveSchema.id],
                        type = row[ActiveSchema.type],
                        quantity = row[ActiveSchema.quantity],
                        executedQuantity = row[ActiveSchema.executedQuantity],
                        cumulativeQuoteQty = row[ActiveSchema.cumulativeQuoteQty],
                        cost = row[ActiveSchema.cost],
                        side = row[ActiveSchema.side],
                        pair = row[ActiveSchema.pair],
                        price = row[ActiveSchema.price],
                        status = row[ActiveSchema.status],
                        createdAt = row[ActiveSchema.createdAt],
                        updatedAt = row[ActiveSchema.updatedAt],
                        cancel = row[ActiveSchema.cancel]
                    )
                }
        }
    }

    suspend fun readCancelID() = dbQuery {
        ActiveSchema.selectAll()
            .where { ActiveSchema.cancel eq true }
            .map { row -> row[ActiveSchema.id].trim() }
    }

    suspend fun save(orders: List<ActiveEntity>) = dbQuery {
        ActiveSchema.batchUpsert(orders) {
            this[ActiveSchema.id] = it.id
            this[ActiveSchema.type] = it.type
            this[ActiveSchema.quantity] = it.quantity
            this[ActiveSchema.executedQuantity] = it.executedQuantity
            this[ActiveSchema.cumulativeQuoteQty] = it.cumulativeQuoteQty
            this[ActiveSchema.cost] = it.cost
            this[ActiveSchema.side] = it.side
            this[ActiveSchema.pair] = it.pair
            this[ActiveSchema.price] = it.price
            this[ActiveSchema.status] = it.status
            this[ActiveSchema.createdAt] = it.createdAt
            this[ActiveSchema.updatedAt] = it.updatedAt
            this[ActiveSchema.cancel] = it.cancel

        }
    }
}