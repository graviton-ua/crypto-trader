package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table

object TradeBookSchema: Table("dao.tradeBook") {
    val id = varchar("id",36)
    val pair = varchar("pair",15)
    val quoteQuantity = double("quoteQuantity")
    val matchPrice = double("matchPrice")
    val matchQuantity = double("matchQuantity")
    val side = varchar("side",12)
    val createdAt = varchar("createdAt",25)
}