package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table

object KunaListSchema : Table("dbo.kunalist") {
    val basecoin = varchar("basecoin", 6)
    val currency = varchar("currency", 6)
    val tikername = varchar("tikername", 10)
    val active = bool("active")

    override val primaryKey = PrimaryKey(tikername)
}