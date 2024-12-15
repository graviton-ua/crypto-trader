package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import ua.cryptogateway.data.db.models.BotConfigEntity
import ua.cryptogateway.data.db.schema.BotConfigsSchema
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class BotConfigsDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    /**
     * Retrieves all entries from the KunaListSchema and maps them to a list of KunaListEntity objects.
     *
     * This function performs a database query to select all records from the KunaListSchema table.
     * Each record is transformed into a KunaListEntity instance, and the function returns a list
     * of these entities.
     *
     * @return A list of KunaListEntity objects representing all records in the KunaListSchema.
     * @throws SQLException If an error occurs during the database query.
     */
    suspend fun getAll() = dbQuery {
        BotConfigsSchema.selectAll()
            .map { row ->
                BotConfigEntity(
                    baseAsset = row[BotConfigsSchema.baseAsset],
                    quoteAsset = row[BotConfigsSchema.quoteAsset],
                    side = row[BotConfigsSchema.side],
                    fond = row[BotConfigsSchema.fond],
                    startPrice = row[BotConfigsSchema.startPrice],
                    priceStep = row[BotConfigsSchema.priceStep],
                    biasPrice = row[BotConfigsSchema.biasPrice],
                    minSize = row[BotConfigsSchema.minSize],
                    orderSize = row[BotConfigsSchema.orderSize],
                    sizeStep = row[BotConfigsSchema.sizeStep],
                    orderAmount = row[BotConfigsSchema.orderAmount],
                    priceForce = row[BotConfigsSchema.priceForce],
                    market = row[BotConfigsSchema.market],
                    basePrec = row[BotConfigsSchema.basePrec],
                    quotePrec = row[BotConfigsSchema.quotePrec],
                    active = row[BotConfigsSchema.active]
                )
            }
    }

    /**
     * Retrieves a list of `KunaListEntity` objects that are marked as active from the database.
     *
     * This function performs a database query that selects all entries from the KunaListSchema
     * where the active flag is set to true. It returns a list of `KunaListEntity` instances
     * created from the results of the query.
     *
     * @return A list of `KunaListEntity` objects with the active status set to true.
     * @throws SQLException If there is an error while executing the database query.
     */
    suspend fun getActive() = dbQuery {
        BotConfigsSchema.selectAll()
            .where { BotConfigsSchema.active eq true }
            .map { row ->
                BotConfigEntity(
                    baseAsset = row[BotConfigsSchema.baseAsset],
                    quoteAsset = row[BotConfigsSchema.quoteAsset],
                    side = row[BotConfigsSchema.side],
                    fond = row[BotConfigsSchema.fond],
                    startPrice = row[BotConfigsSchema.startPrice],
                    priceStep = row[BotConfigsSchema.priceStep],
                    biasPrice = row[BotConfigsSchema.biasPrice],
                    minSize = row[BotConfigsSchema.minSize],
                    orderSize = row[BotConfigsSchema.orderSize],
                    sizeStep = row[BotConfigsSchema.sizeStep],
                    orderAmount = row[BotConfigsSchema.orderAmount],
                    priceForce = row[BotConfigsSchema.priceForce],
                    market = row[BotConfigsSchema.market],
                    basePrec = row[BotConfigsSchema.basePrec],
                    quotePrec = row[BotConfigsSchema.quotePrec],
                    active = row[BotConfigsSchema.active]
                )
            }
    }

    /**
     * Retrieves a list of active ticker names from the KunaListSchema.
     *
     * This function executes a database query to select all ticker names from the KunaListSchema
     * where the 'active' flag is set to true. It returns a list of these ticker names.
     *
     * @return A list of String representing the active ticker names.
     */
    suspend fun getActiveTickers() = dbQuery {
        BotConfigsSchema.selectAll()
//            .where { (BotConfigsSchema.active eq true) and (BotConfigsSchema.side eq Side.fromKunaString("AAA")) }
            .where { (BotConfigsSchema.active eq true) and (BotConfigsSchema.side eq Order.Side.Sell) }
            .map { row -> (row[BotConfigsSchema.baseAsset] + "_" + row[BotConfigsSchema.quoteAsset]).trim() }
    }
}