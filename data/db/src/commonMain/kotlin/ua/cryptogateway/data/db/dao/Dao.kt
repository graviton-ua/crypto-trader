package ua.cryptogateway.data.db.dao

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ua.cryptogateway.data.db.CryptoDb

internal interface Dao {
    val dispatcher: CoroutineDispatcher
    val database: Database

    suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T =
        newSuspendedTransaction(context = dispatcher, db = database) { block() }
}

abstract class SqlDelightDao(
    protected val dispatcher: CoroutineDispatcher,
    protected val db: CryptoDb,
) {
    suspend fun <T> transaction(block: CryptoDb.() -> T): T = withContext(dispatcher) { db.transactionWithResult { block(db) } }
}