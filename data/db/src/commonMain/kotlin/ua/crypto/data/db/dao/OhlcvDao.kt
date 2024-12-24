package ua.crypto.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.models.OhlcvEntity
import ua.crypto.data.sql.Database

@Inject
class OhlcvDao(
    dispatchers: AppCoroutineDispatchers,
    db: Database,
) : Dao(dispatcher = dispatchers.io, db = db) {

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

    suspend fun getAllClosedForPeriod(pair: String, minutes: Int) = transaction {
        ohlcvQueries.getClosedForPeriod(pair = pair, minutes = minutes.toLong(), mapper = mapperPeriod).executeAsList()
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
private val mapperPeriod: (
    grp_id: Long?,
    pair: String,
    open_time: Instant?,
    close_time: Instant?,
    trades: Long?,
    open_price: Array<Double>,
    close_price: Array<Double>,
    low_price: Double?,
    high_price: Double?,
    volume: Double?,
    updated_at: Instant?,
) -> OhlcvEntity = { grp_id,
                     pair,
                     open_time,
                     close_time,
                     trades,
                     open_price,
                     close_price,
                     low_price,
                     high_price,
                     volume,
                     updated_at ->
    OhlcvEntity(
        id = grp_id?.toInt()!!,
        pair = pair,
        openTime = open_time!!,
        closeTime = close_time!!,
        trades = trades?.toInt()!!,
        openPrice = open_price[0],
        highPrice = high_price!!,
        lowPrice = low_price!!,
        closePrice = close_price[0],
        volume = volume!!,
        timestamp = updated_at!!
    )
}