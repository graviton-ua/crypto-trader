package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table

object BookAskSchema : Table("dbo.bookAsk") {
    val price = double("price")
    val volume = double("volume")
    val sumVolume=double("sumVolume")
}