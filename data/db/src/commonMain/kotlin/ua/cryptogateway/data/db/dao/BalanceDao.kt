package ua.cryptogateway.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.Database
import ua.cryptogateway.data.db.models.BalanceEntity
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class BalanceDao(
    dispatchers: AppCoroutineDispatchers,
    db: Database,
) : Dao(dispatcher = dispatchers.io, db = db) {


    suspend fun getAll() = transaction {
        balanceQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun getForAsset(asset: String) = transaction {
        balanceQueries.getForAsset(asset = asset, mapper = mapper).executeAsOneOrNull()
    }

    suspend fun save(entities: List<BalanceEntity>) = Result.runCatching {
        transaction {
            entities.forEach {
                balanceQueries.save(
                    asset = it.currency,
                    balance = it.balance,
                    lockBalance = it.lockBalance,
                    entire = it.entire,
                )
            }
        }
    }
}

private val mapper: (String, Double, Double, Double, Instant) -> BalanceEntity = ::BalanceEntity