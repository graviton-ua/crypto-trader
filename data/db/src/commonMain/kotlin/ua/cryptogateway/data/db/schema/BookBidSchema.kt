package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table

object BookBidSchema : Table("dbo.bookBid") {
    val price = double("price")
    val volume = double("volume")
    val sumVolume = double("sumVolume")
}