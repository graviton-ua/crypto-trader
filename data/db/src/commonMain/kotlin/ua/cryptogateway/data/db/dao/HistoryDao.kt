package ua.cryptogateway.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.CryptoDb
import ua.cryptogateway.data.db.models.HistoryEntity
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class HistoryDao(
    dispatchers: AppCoroutineDispatchers,
    db: CryptoDb,
) : SqlDelightDao(dispatcher = dispatchers.io, db = db) {

    suspend fun getAll(): List<HistoryEntity> = transaction {
        historyQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun save(entities: List<HistoryEntity>) = Result.runCatching {
        transaction {
            entities.forEach { entity ->
                historyQueries.save(
                    id = entity.id,
                    orderId = entity.orderId,
                    pair = entity.pair,
                    quantity = entity.quantity,
                    price = entity.price,
                    isTaker = entity.isTaker,
                    fee = entity.fee,
                    feeAsset = entity.feeCurrency,
                    isBuyer = entity.isBuyer,
                    quoteQuantity = entity.quoteQuantity,
                    createdAt = entity.createdAt,
                )
            }
        }
    }
}

private val mapper: (String, String, String, Double, Double, Boolean, Double, String, Boolean, Double, Instant) -> HistoryEntity = ::HistoryEntity