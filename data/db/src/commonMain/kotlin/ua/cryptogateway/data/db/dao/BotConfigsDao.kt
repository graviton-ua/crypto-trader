package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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


    suspend fun save(entity: BotConfigEntity) = Result.runCatching {
        dbQuery {
            BotConfigsSchema.upsert {
                it[BotConfigsSchema.baseAsset] = entity.baseAsset
                it[BotConfigsSchema.quoteAsset] = entity.quoteAsset
                it[BotConfigsSchema.side] = entity.side
                it[BotConfigsSchema.fond] = entity.fond
                it[BotConfigsSchema.startPrice] = entity.startPrice
                it[BotConfigsSchema.priceStep] = entity.priceStep
                it[BotConfigsSchema.biasPrice] = entity.biasPrice
                it[BotConfigsSchema.minSize] = entity.minSize
                it[BotConfigsSchema.orderSize] = entity.orderSize
                it[BotConfigsSchema.sizeStep] = entity.sizeStep
                it[BotConfigsSchema.orderAmount] = entity.orderAmount
                it[BotConfigsSchema.priceForce] = entity.priceForce
                it[BotConfigsSchema.market] = entity.market
                it[BotConfigsSchema.basePrec] = entity.basePrec
                it[BotConfigsSchema.quotePrec] = entity.quotePrec
                it[BotConfigsSchema.active] = entity.active
            }
        }
    }

    suspend fun save(
        baseAsset: String, quoteAsset: String, side: Order.Side,
        fond: Double, startPrice: Double, priceStep: Double,
        biasPrice: Double, minSize: Double, orderSize: Int,
        sizeStep: Double, orderAmount: Int, priceForce: Boolean,
        market: Boolean, basePrec: Int, quotePrec: Int, active: Boolean,
    ) = Result.runCatching {
        dbQuery {
            BotConfigsSchema.upsert {
                it[BotConfigsSchema.baseAsset] = baseAsset
                it[BotConfigsSchema.quoteAsset] = quoteAsset
                it[BotConfigsSchema.side] = side
                it[BotConfigsSchema.fond] = fond
                it[BotConfigsSchema.startPrice] = startPrice
                it[BotConfigsSchema.priceStep] = priceStep
                it[BotConfigsSchema.biasPrice] = biasPrice
                it[BotConfigsSchema.minSize] = minSize
                it[BotConfigsSchema.orderSize] = orderSize
                it[BotConfigsSchema.sizeStep] = sizeStep
                it[BotConfigsSchema.orderAmount] = orderAmount
                it[BotConfigsSchema.priceForce] = priceForce
                it[BotConfigsSchema.market] = market
                it[BotConfigsSchema.basePrec] = basePrec
                it[BotConfigsSchema.quotePrec] = quotePrec
                it[BotConfigsSchema.active] = active
            }
        }
    }

    suspend fun delete(
        baseAsset: String, quoteAsset: String, side: Order.Side,
    ) = Result.runCatching {
        dbQuery {
            BotConfigsSchema.deleteWhere {
                (BotConfigsSchema.baseAsset eq baseAsset) and (BotConfigsSchema.quoteAsset eq quoteAsset) and (BotConfigsSchema.side eq side)
            }
        }
    }
}