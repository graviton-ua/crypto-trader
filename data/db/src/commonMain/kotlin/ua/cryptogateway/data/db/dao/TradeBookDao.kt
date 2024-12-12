package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchUpsert
import ua.cryptogateway.data.db.models.TradeBookEntity
import ua.cryptogateway.data.db.schema.TradeBookSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class TradeBookDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    suspend fun save(order: List<TradeBookEntity>) = Result.runCatching {
        dbQuery {
            TradeBookSchema.batchUpsert(order) {
                this[TradeBookSchema.id] = it.id
                this[TradeBookSchema.pair] = it.pair
                this[TradeBookSchema.quoteQuantity] = it.quoteQuantity
                this[TradeBookSchema.matchPrice] = it.matchPrice
                this[TradeBookSchema.matchQuantity] = it.matchQuantity
                this[TradeBookSchema.side] = it.side
                this[TradeBookSchema.createdAt] = it.createdAt
            }
        }
    }
}