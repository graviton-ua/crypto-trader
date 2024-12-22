package ua.cryptogateway.data.db.dao

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.CryptoDb
import ua.cryptogateway.data.db.models.OrderBookEntity
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class OrderBookDao(
    dispatchers: AppCoroutineDispatchers,
    db: CryptoDb,
) : SqlDelightDao(dispatcher = dispatchers.io, db = db) {

    suspend fun getAll(): List<OrderBookEntity> = transaction {
        orderbookQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun get(pair: String): OrderBookEntity? = transaction {
        orderbookQueries.getForPair(pair = pair, mapper = mapper).executeAsOneOrNull()
    }

    suspend fun save(entity: OrderBookEntity) = Result.runCatching {
        transaction {
            orderbookQueries.save(
                pair = entity.pair,
                asks = Json.encodeToString(entity.asks),
                bids = Json.encodeToString(entity.bids),
            )
        }
    }
}

private val mapper: (String, String, String) -> OrderBookEntity = { pair, asks, bids ->
    OrderBookEntity(pair = pair, asks = Json.decodeFromString(asks), bids = Json.decodeFromString(bids))
}