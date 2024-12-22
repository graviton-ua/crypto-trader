package ua.cryptogateway.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.CryptoDb
import ua.cryptogateway.data.db.models.OhlcvEntity
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OhlcvDao(
    dispatchers: AppCoroutineDispatchers,
    db: CryptoDb,
) : SqlDelightDao(dispatcher = dispatchers.io, db = db) {

    suspend fun getAll() = transaction {
        ohlcvQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun getAll(pair: String) = transaction {
        ohlcvQueries.getForPair(pair = pair, mapper = mapper).executeAsList()
    }

    suspend fun get(id: Int) = transaction {
        ohlcvQueries.getForId(id = id, mapper = mapper).executeAsOneOrNull()
    }


    suspend fun getAllClosedForEachMinute(pair: String) = transaction {
        ohlcvQueries.getClosedForEachMinute(pair = pair, mapper = mapper).executeAsList()
    }


    /**
     * Saves the given OHLCV entity to the database. This method performs an upsert operation on the
     * OhlcvSchema table, updating existing records or inserting new ones based on the entity's data.
     *
     * @param entity The OhlcvEntity object containing the data to be saved in the database.
     */
    suspend fun save(entity: OhlcvEntity) = Result.runCatching {
        transaction {
            ohlcvQueries.insert(
                entity.pair,
                entity.openTime,
                entity.closeTime,
                entity.trades,
                entity.openPrice,
                entity.highPrice,
                entity.lowPrice,
                entity.closePrice,
                entity.volume,
            )
        }
    }
}

private val mapper: (Int, String, Instant, Instant, Int, Double, Double, Double, Double, Double, Instant) -> OhlcvEntity = ::OhlcvEntity