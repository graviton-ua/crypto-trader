package ua.cryptogateway.data.db.schema

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object BalanceSchema : Table("balance") {
    val currency = varchar("currency", 4)
    val balance = double("balance")
    val lockBalance = double("lockBalance")
    val entire  = double("entire")
    val timestamp = timestamp("timestamp").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(currency)
}