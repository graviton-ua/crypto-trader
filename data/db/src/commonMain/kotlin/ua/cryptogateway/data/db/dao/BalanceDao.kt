package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.selectAll
import ua.cryptogateway.data.db.models.BalanceEntity
import ua.cryptogateway.data.db.schema.BalanceSchema
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class BalanceDao(
    dispatchers: AppCoroutineDispatchers,
    override val database: Database,
) : Dao {
    override val dispatcher = dispatchers.io


    suspend fun getAll() = dbQuery {
        BalanceSchema.selectAll()
            .map { row ->
                BalanceEntity(
                    currency = row[BalanceSchema.currency],
                    entire = row[BalanceSchema.entire],
                    available = row[BalanceSchema.available],
                    timestamp = row[BalanceSchema.timestamp]
                )
            }
    }

    suspend fun save(entities: List<BalanceEntity>) = dbQuery {
        BalanceSchema.batchUpsert(entities) {
            this[BalanceSchema.currency] = it.currency
            this[BalanceSchema.entire] = it.entire
            this[BalanceSchema.available] = it.available
            this[BalanceSchema.timestamp] = it.timestamp
        }
    }
}