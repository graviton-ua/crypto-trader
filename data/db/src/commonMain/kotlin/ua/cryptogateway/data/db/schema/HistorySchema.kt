package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object HistorySchema :Table("history") {
    val id = varchar("id",36)
    val orderId = varchar("orderId",36)
    val pair = varchar("pair",15)
    val quantity = double("quantity")
    val price = double("price")
    val isTaker = bool("isTaker")
    val fee = double("fee")
    val feeCurrency = varchar("feeCurrency",6)
    val isBuyer = bool("isBuyer")
    val quoteQuantity = double("quoteQuantity")
    val createdAt = timestamp("createdAt")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}