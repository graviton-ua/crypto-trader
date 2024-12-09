package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.selectAll
import ua.cryptogateway.data.db.models.TickerEntity
import ua.cryptogateway.data.db.models.UpdateActiveOrdersEntity
import ua.cryptogateway.data.db.schema.TickerSchema
import ua.cryptogateway.data.db.schema.UpdateActiveOrdersSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class TickersDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io


    suspend fun getAll(): List<TickerEntity> = dbQuery {
        TickerSchema.selectAll()
            //.where { Users.id eq id }
            .map { row ->
                TickerEntity(
                    pairName = row[TickerSchema.pairName],
                    priceHigh = row[TickerSchema.priceHigh],
                    priceAsk = row[TickerSchema.priceAsk],
                    priceBid = row[TickerSchema.priceBid],
                    priceLow = row[TickerSchema.priceLow],
                    priceLast = row[TickerSchema.priceLast],
                    change = row[TickerSchema.change],
                    timestamp = row[TickerSchema.timestamp],
                )
            }
    }

    suspend fun getByPairName(pairName: String): TickerEntity? = dbQuery {
        TickerSchema.selectAll()
            .where { TickerSchema.pairName eq pairName }
            .map { row ->
                TickerEntity(
                    pairName = row[TickerSchema.pairName],
                    priceHigh = row[TickerSchema.priceHigh],
                    priceAsk = row[TickerSchema.priceAsk],
                    priceBid = row[TickerSchema.priceBid],
                    priceLow = row[TickerSchema.priceLow],
                    priceLast = row[TickerSchema.priceLast],
                    change = row[TickerSchema.change],
                    timestamp = row[TickerSchema.timestamp],
                )
            }.firstOrNull()
    }


    suspend fun save(tickers: List<TickerEntity>) = dbQuery {
        TickerSchema.batchUpsert(tickers) {
            this[TickerSchema.pairName] = it.pairName
            this[TickerSchema.priceHigh] = it.priceHigh
            this[TickerSchema.priceAsk] = it.priceAsk
            this[TickerSchema.priceBid] = it.priceBid
            this[TickerSchema.priceLow] = it.priceLow
            this[TickerSchema.priceLast] = it.priceLast
            this[TickerSchema.change] = it.change
            this[TickerSchema.timestamp] = it.timestamp
        }
    }

    suspend fun updateActiveOrders(tickers: List<UpdateActiveOrdersEntity>) = dbQuery {
        UpdateActiveOrdersSchema.batchUpsert(tickers) {
            this[UpdateActiveOrdersSchema.pairName] = it.pairName
            this[UpdateActiveOrdersSchema.priceAsk] = it.priceAsk
            this[UpdateActiveOrdersSchema.priceBid] = it.priceBid
            this[UpdateActiveOrdersSchema.change] = it.change
        }
    }
}