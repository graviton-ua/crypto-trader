package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TradeBookSchema : Table("tradeBook") {
    val id = varchar("id", 36)
    val pair = varchar("pair", 15)
    val quoteQuantity = double("quoteQuantity")
    val matchPrice = double("matchPrice")
    val matchQuantity = double("matchQuantity")
    val side = varchar("side", 12)
    val createdAt = timestamp("createdAt")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}