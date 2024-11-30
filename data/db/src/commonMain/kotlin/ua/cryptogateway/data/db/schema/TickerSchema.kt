package ua.cryptogateway.data.db.schema

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TickerSchema : Table("dbo.tickers") {
    val pairName = varchar("pairName", 12)
    val priceHigh = double("priceHigh")
    val priceAsk = double("priceAsk")
    val priceBid = double("priceBid")
    val priceLow = double("priceLow")
    val priceLast = double("priceLast")
    val change = double("change")
    val timestamp = timestamp("timestamp").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(pairName)
}