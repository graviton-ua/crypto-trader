package ua.crypto.data.db.dao

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.models.CandleEntity
import ua.crypto.data.sql.Database
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

@Inject
class CandlesDao(
    dispatchers: AppCoroutineDispatchers,
    db: Database,
) : Dao(dispatcher = dispatchers.io, db = db) {

    suspend fun getAll() = transaction {
        candlesQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun getByPair(pair: String) = transaction {
        candlesQueries.getByPair(pair = pair, mapper = mapper).executeAsList()
    }

    suspend fun getByPairInterval(pair: String, interval: Duration, duration: Duration = 24.hours) = transaction {
        val minutesInterval = interval.inWholeMinutes
        val durationAgo = Clock.System.now().minus(duration)
        candlesQueries.getByPairInterval(pair = pair, minutes = minutesInterval, time = durationAgo, mapper = mapperPeriod).executeAsList()
    }


    /**
     * Saves the given OHLCV entity to the database. This method performs an upsert operation on the
     * OhlcvSchema table, updating existing records or inserting new ones based on the entity's data.
     *
     * @param entity The OhlcvEntity object containing the data to be saved in the database.
     */
    suspend fun save(entity: CandleEntity) = Result.runCatching {
        transaction {
            candlesQueries.insert(
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

private val mapper: (Int, String, Instant, Instant, Int, Double, Double, Double, Double, Double, Instant) -> CandleEntity = ::CandleEntity
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
) -> CandleEntity = { grp_id,
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
    CandleEntity(
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