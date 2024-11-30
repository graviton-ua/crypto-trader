package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table

object OrdersGridSchema : Table(name = "dbo.ordersgrid") {
    val pairname = varchar("pairname", 12).nullable()
    val side = varchar("side", 4).nullable()
    val remvolume = double("remvolume").nullable()
    val volume = double("volume").nullable()
    val pricebid = double("pricebid").nullable()
    val price = double("price").nullable()
    val priceask = double("priceask").nullable()
    val change = float("change").nullable()
    val gecko = double("gecko").nullable()
    val offset = float("offset").nullable()
    val smashort = double("smashort").nullable()
    val smalong = double("smalong").nullable()
    val atr = integer("atr").nullable()
    val id = varchar("id", 50).nullable()
    val status = varchar("status", 15).nullable()
    val ordtype = varchar("ordtype", 15).nullable()
    val deleteord = bool("deleteord").nullable()
    val timemarket = varchar("timemarket",25).nullable()
    val lost = bool("lost").nullable()
}