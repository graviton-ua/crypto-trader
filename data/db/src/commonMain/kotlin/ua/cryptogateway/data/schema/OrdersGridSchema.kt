package ua.cryptogateway.data.schema

import org.jetbrains.exposed.sql.Table

object OrdersGridSchema : Table(name = "dbo.ordersgrid") {
    val pairname = varchar("pairname", 12).nullable()
    val side = varchar("side", 4).nullable()
}