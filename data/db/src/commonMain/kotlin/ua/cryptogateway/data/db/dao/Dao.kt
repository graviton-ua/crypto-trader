package ua.cryptogateway.data.db.dao

import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

internal interface Dao {
    val dispatcher: CoroutineDispatcher
    val database: Database

    suspend fun <T> dbQuery(block: suspend Transaction.() -> T): T =
        newSuspendedTransaction(context = dispatcher, db = database) { block() }
}