package ua.crypto.data.db.dao

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.models.CandleEntity
import ua.crypto.data.models.CryptoPlatform
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

    suspend fun getByPair(platform: CryptoPlatform, pair: String) = transaction {
        candlesQueries.getByPair(platform = platform, pair = pair, mapper = mapper).executeAsList()
    }

    /**
     *  SELECT
     *     2 * 60 * FLOOR(EXTRACT(EPOCH FROM open_time) / (2 * 60)) AS grp_id,
     * 	platform,
     *     pair,
     *     MIN(open_time) AS open_time,
     *     MAX(close_time) AS close_time,
     * 	SUM(trades) AS trades,
     *   	(ARRAY_AGG(open_price ORDER BY open_time ASC))[1] AS open_price,
     *   	(ARRAY_AGG(close_price ORDER BY open_time DESC))[1] AS close_price,
     *     MIN(low_price) AS low_price,
     *     MAX(high_price) AS high_price,
     *     SUM(volume) AS volume,
     *     MAX(updated_at) AS updated_at
     * FROM
     *     candles
     * WHERE pair = 'BTC_USDT' AND platform = 'KUNA'
     * GROUP BY
     *     platform, pair, grp_id
     * ORDER BY
     *     grp_id;
     */
    suspend fun getByPairInterval(platform: CryptoPlatform, pair: String, interval: Duration, duration: Duration = 24.hours) = transaction {
        val minutesInterval = interval.inWholeMinutes
        val durationAgo = Clock.System.now().minus(duration)
        candlesQueries.getByPairInterval(
            platform = platform,
            pair = pair,
            minutes = minutesInterval,
            time = durationAgo,
            mapper = mapperPeriod,
        ).executeAsList()
    }

    suspend fun getByPairInterval(platform: CryptoPlatform, pair: String, interval: Duration, crsiLength: Int) =
        getByPairInterval(platform, pair = pair, interval = interval, duration = interval * (crsiLength + 1))


    /**
     * Saves the given OHLCV entity to the database. This method performs an upsert operation on the
     * OhlcvSchema table, updating existing records or inserting new ones based on the entity's data.
     *
     * @param entity The OhlcvEntity object containing the data to be saved in the database.
     */
    suspend fun save(entity: CandleEntity) = Result.runCatching {
        transaction {
            candlesQueries.insert(
                entity.platform,
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

private val mapper: (CryptoPlatform, String, Instant, Instant, Int, Double, Double, Double, Double, Double, Instant) -> CandleEntity = ::CandleEntity
private val mapperPeriod: (
    grp_id: Long?, platform: CryptoPlatform, pair: String, open_time: Instant?, close_time: Instant?, trades: Long?,
    open_price: Array<Double>, close_price: Array<Double>, low_price: Double?, high_price: Double?,
    volume: Double?, updated_at: Instant?,
) -> CandleEntity = { _, platform: CryptoPlatform, pair: String, open_time: Instant?, close_time: Instant?, trades: Long?,
                      open_price: Array<Double>, close_price: Array<Double>, low_price: Double?, high_price: Double?,
                      volume: Double?, updated_at: Instant? ->
    CandleEntity(
        platform = platform,
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

// =================
// Migration Ohlcv -> Candles
// =================
//
//-- Step 1: Rename the table
//ALTER TABLE ohlcv RENAME TO candles;
//
//-- Step 2: Add the `platform` column with a default value, then remove the default
//ALTER TABLE candles
//ADD COLUMN platform VARCHAR(20) NOT NULL DEFAULT 'KUNA';
//ALTER TABLE candles
//ALTER COLUMN platform DROP DEFAULT;
//
//-- Step 3: Drop the `id` column
//ALTER TABLE candles DROP COLUMN id;
//
//-- Step 4: Drop the old unique constraint
//ALTER TABLE candles DROP CONSTRAINT unique_pair_open_close_time;
//
//-- Step 5: Add the new primary key
//ALTER TABLE candles
//ADD CONSTRAINT candles_pkey PRIMARY KEY (platform, pair, open_time, close_time);
