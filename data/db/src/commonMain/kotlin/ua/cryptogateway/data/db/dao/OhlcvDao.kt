package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.*
import ua.cryptogateway.data.db.models.OhlcvEntity
import ua.cryptogateway.data.db.schema.BotConfigsSchema
import ua.cryptogateway.data.db.schema.OhlcvSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OhlcvDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    /**
     * Retrieves all records from the OhlcvSchema table and maps them to a list of OhlcvEntity objects.
     * This method performs a database query using a coroutine context and suspends execution until the query is complete.
     *
     * @return A list of OhlcvEntity objects representing the records in the OhlcvSchema table.
     */
    suspend fun getAll() = dbQuery {
        OhlcvSchema.selectAll()
            .mapOhlcvEntities()
    }

    /**
     * Retrieves all records from the OhlcvSchema table that match the specified trading pair
     * and maps them to a list of OhlcvEntity objects.
     *
     * @param pair The trading pair to filter records by.
     * @return A list of OhlcvEntity objects representing the filtered records.
     */
    suspend fun getAll(pair: String) = dbQuery {
        OhlcvSchema.selectAll()
            .where { OhlcvSchema.pair eq pair }
            .mapOhlcvEntities()
    }

    //SELECT *
    //FROM ohlcv
    //JOIN (
    //    SELECT
    //        pair,
    //        closeTime,
    //        MAX([timestamp]) AS timestamp
    //    FROM ohlcv
    //    WHERE pair = 'BTC_USDT'
    //    GROUP BY pair, closeTime
    //) AS LatestRows
    //ON ohlcv.pair = LatestRows.pair
    //   AND ohlcv.closeTime = LatestRows.closeTime
    //   AND ohlcv.timestamp = LatestRows.timestamp
    //ORDER BY ohlcv.closeTime ASC;
    /**
     * Retrieves all OHLCV (Open, High, Low, Close, Volume) records for the specified trading pair,
     * grouped by each minute and ordered by the close time. This method only includes the most
     * recent record for each minute based on the timestamp.
     *
     * @param pair The trading pair for which to retrieve the records (e.g., "BTC_USD").
     */
    suspend fun getAllClosedForEachMinute(pair: String) = dbQuery {
        val latestRows = OhlcvSchema
            .select(
                OhlcvSchema.pair,
                OhlcvSchema.closeTime,
                OhlcvSchema.timestamp.max().alias("timestamp")
            )
            .where { OhlcvSchema.pair eq pair }
            .groupBy(OhlcvSchema.pair, OhlcvSchema.closeTime)
            .alias("LatestRows")

        // Define aliased columns
        val latestPairColumn = latestRows[OhlcvSchema.pair]
        val latestCloseTimeColumn = latestRows[OhlcvSchema.closeTime]
        val maxTimestampColumn = latestRows[OhlcvSchema.timestamp]

        // Main query joining with the subquery
        OhlcvSchema
            .join(
                latestRows, JoinType.INNER,
                additionalConstraint = {
                    (OhlcvSchema.pair eq latestPairColumn) and
                    (OhlcvSchema.closeTime eq latestCloseTimeColumn) and
                            (OhlcvSchema.timestamp eq maxTimestampColumn)
                }
            )
            .selectAll()
            .orderBy(OhlcvSchema.closeTime to SortOrder.ASC)
            .mapOhlcvEntities()
    }

    /**
     * Retrieves a single OhlcvEntity record from the database that matches the provided ID.
     *
     * @param id The unique identifier of the OhlcvEntity record to retrieve.
     * @return An OhlcvEntity object if found, or null if no record matches the specified ID.
     */
    suspend fun get(id: Int) = dbQuery {
        OhlcvSchema.selectAll()
            .where { BotConfigsSchema.id eq id }
            .mapOhlcvEntities()
            .firstOrNull()
    }


    /**
     * Saves the given OHLCV entity to the database. This method performs an upsert operation on the
     * OhlcvSchema table, updating existing records or inserting new ones based on the entity's data.
     *
     * @param entity The OhlcvEntity object containing the data to be saved in the database.
     */
    suspend fun save(entity: OhlcvEntity) = Result.runCatching {
        dbQuery {
            OhlcvSchema.upsert {
                it[OhlcvSchema.id] = entity.id
                it[OhlcvSchema.pair] = entity.pair
                it[OhlcvSchema.openTime] = entity.openTime
                it[OhlcvSchema.closeTime] = entity.closeTime
                it[OhlcvSchema.trades] = entity.trades
                it[OhlcvSchema.openPrice] = entity.openPrice
                it[OhlcvSchema.highPrice] = entity.highPrice
                it[OhlcvSchema.lowPrice] = entity.lowPrice
                it[OhlcvSchema.closePrice] = entity.closePrice
                it[OhlcvSchema.volume] = entity.volume
                it[OhlcvSchema.timestamp] = entity.timestamp
            }
        }
    }
}

private fun Query.mapOhlcvEntities(): List<OhlcvEntity> = map { row ->
    OhlcvEntity(
        id = row[OhlcvSchema.id],
        pair = row[OhlcvSchema.pair],
        openTime = row[OhlcvSchema.openTime],
        closeTime = row[OhlcvSchema.closeTime],
        trades = row[OhlcvSchema.trades],
        openPrice = row[OhlcvSchema.openPrice],
        highPrice = row[OhlcvSchema.highPrice],
        lowPrice = row[OhlcvSchema.lowPrice],
        closePrice = row[OhlcvSchema.closePrice],
        volume = row[OhlcvSchema.volume],
        timestamp = row[OhlcvSchema.timestamp],
    )
}