package ua.cryptogateway.data.db.dao

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ua.cryptogateway.data.db.Database

abstract class Dao(
    protected val dispatcher: CoroutineDispatcher,
    protected val db: Database,
) {
    suspend fun <T> transaction(block: Database.() -> T): T = withContext(dispatcher) { db.transactionWithResult { block(db) } }
}