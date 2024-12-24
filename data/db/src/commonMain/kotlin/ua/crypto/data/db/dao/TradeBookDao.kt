package ua.crypto.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.models.TradeBookEntity
import ua.crypto.data.sql.Database

@Inject
class TradeBookDao(
    dispatchers: AppCoroutineDispatchers,
    db: Database,
) : Dao(dispatcher = dispatchers.io, db = db) {

    suspend fun getAll(): List<TradeBookEntity> = transaction {
        trade_booksQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun save(entities: List<TradeBookEntity>) = Result.runCatching {
        transaction {
            entities.forEach { entity ->
                trade_booksQueries.save(
                    id = entity.id,
                    pair = entity.pair,
                    quoteQuantity = entity.quoteQuantity,
                    matchPrice = entity.matchPrice,
                    matchQuantity = entity.matchQuantity,
                    side = entity.side,
                    createdAt = entity.createdAt,
                )
            }
        }
    }
}

private val mapper: (String, String, Double, Double, Double, String, Instant) -> TradeBookEntity = ::TradeBookEntity