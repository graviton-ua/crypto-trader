package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchUpsert
import ua.cryptogateway.data.db.models.HistoryEntity
import ua.cryptogateway.data.db.schema.HistorySchema
import ua.cryptogateway.util.AppCoroutineDispatchers


@Inject
class HistoryDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    suspend fun save(order: List<HistoryEntity>) = Result.runCatching {
        dbQuery {
            HistorySchema.batchUpsert(order){
                this[HistorySchema.id] = it.id
                this[HistorySchema.orderId] = it.orderId
                this[HistorySchema.pair] = it.pair
                this[HistorySchema.quantity] = it.quantity
                this[HistorySchema.price] = it.price
                this[HistorySchema.isTaker] = it.isTaker
                this[HistorySchema.fee] = it.fee
                this[HistorySchema.feeCurrency] = it.feeCurrency
                this[HistorySchema.isBuyer] = it.isBuyer
                this[HistorySchema.quoteQuantity] = it.quoteQuantity
                this[HistorySchema.createdAt] = it.createdAt
            }
        }
    }
}