package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import ua.cryptogateway.data.db.models.OrderBookEntity
import ua.cryptogateway.data.db.schema.OrderBookSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OrderBookDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io

    suspend fun getAll(): List<OrderBookEntity> = dbQuery {
        OrderBookSchema.selectAll()
            .map { row ->
                OrderBookEntity(
                    pair = row[OrderBookSchema.pair],
                    asks = row[OrderBookSchema.asks],
                    bids = row[OrderBookSchema.bids]
                )
            }
    }

    suspend fun get(pair: String): OrderBookEntity? = dbQuery {
        OrderBookSchema.selectAll()
            .where { OrderBookSchema.pair eq pair }
            .map { row ->
                OrderBookEntity(
                    pair = row[OrderBookSchema.pair],
                    asks = row[OrderBookSchema.asks],
                    bids = row[OrderBookSchema.bids]
                )
            }
            .firstOrNull()
    }
}