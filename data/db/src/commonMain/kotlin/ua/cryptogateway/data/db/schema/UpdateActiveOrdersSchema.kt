package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table

object UpdateActiveOrdersSchema : Table("dbo.tickers") {
    val pairName = varchar("pairName", 12)
    val priceAsk = double("priceAsk")
    val priceBid = double("priceBid")
    val change = double("change")
    }