package ua.cryptogateway.data.db.schema

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object OhlcvSchema : Table("ohlcv") {
    val id = integer("id").autoIncrement()

    val pair = varchar("pair", 15)

    val openTime = timestamp("openTime")
    val closeTime = timestamp("closeTime")
    val trades = integer("trades").default(0)
    val openPrice = double("openPrice").default(0.0)
    val highPrice = double("highPrice").default(0.0)
    val lowPrice = double("lowPrice").default(0.0)
    val closePrice = double("closePrice").default(0.0)
    val volume = double("volume").default(0.0)

    val timestamp = timestamp("timestamp").clientDefault { Clock.System.now() }


    override val primaryKey = PrimaryKey(id)
}