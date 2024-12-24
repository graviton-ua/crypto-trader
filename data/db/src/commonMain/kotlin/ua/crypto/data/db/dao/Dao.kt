package ua.crypto.data.db.dao

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ua.crypto.data.sql.Database

abstract class Dao(
    protected val dispatcher: CoroutineDispatcher,
    protected val db: Database,
) {
    suspend fun <T> transaction(block: Database.() -> T): T = withContext(dispatcher) { db.transactionWithResult { block(db) } }
}