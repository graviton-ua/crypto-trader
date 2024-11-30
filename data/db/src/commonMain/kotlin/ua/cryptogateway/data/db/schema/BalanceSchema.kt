package ua.cryptogateway.data.db.schema

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object BalanceSchema : Table("dbo.balance") {
    val currency = varchar("currency", 4)
    val entire = double("entire")
    val available = double("available")
    val timestamp = timestamp("timestamp").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(currency)
}