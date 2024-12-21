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
                    balance = row[BalanceSchema.balance],
                    lockBalance = row[BalanceSchema.lockBalance],
                    entire = row[BalanceSchema.entire],
                    timestamp = row[BalanceSchema.timestamp]
                )
            }
    }

    suspend fun getCurrency(currency: String) = dbQuery {
        BalanceSchema.selectAll()
            .where { BalanceSchema.currency eq currency }
            .map { row ->
                BalanceEntity(
                    currency = row[BalanceSchema.currency],
                    balance = row[BalanceSchema.balance],
                    lockBalance = row[BalanceSchema.lockBalance],
                    entire = row[BalanceSchema.entire],
                    timestamp = row[BalanceSchema.timestamp]
                )
            }.firstOrNull()
    }

    suspend fun save(entities: List<BalanceEntity>) = Result.runCatching {
        dbQuery {
            BalanceSchema.batchUpsert(entities) {
                this[BalanceSchema.currency] = it.currency
                this[BalanceSchema.balance] = it.balance
                this[BalanceSchema.lockBalance] = it.lockBalance
                this[BalanceSchema.entire] = it.entire
                this[BalanceSchema.timestamp] = it.timestamp
            }
        }
    }
}