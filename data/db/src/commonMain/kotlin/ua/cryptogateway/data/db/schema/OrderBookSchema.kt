package ua.cryptogateway.data.db.schema

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json

object OrderBookSchema : Table("dbo.orderbook") {
    val pair = varchar("pair", 12)
    val asks = json<List<List<Double>>>(
        name = "asks",
        serialize = { Json.encodeToString(it) },
        deserialize = { Json.decodeFromString(it) },
    )
    val bids = json<List<List<Double>>>(
        name = "bids",
        serialize = { Json.encodeToString(it) },
        deserialize = { Json.decodeFromString(it) },
    )

    override val primaryKey: PrimaryKey = PrimaryKey(pair)
}