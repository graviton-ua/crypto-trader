package ua.cryptogateway.data.db.schema

import org.jetbrains.exposed.sql.Table

//{
//    "id": "5992a049-8612-409d-8599-2c3d7298b106",            // Unique identifier of an order
//    "type": "Limit",                                         // Type of an order
//    "quantity": "5",                                         // Original order quantity
//    "executedQuantity": "0",                                 // Traded quantity in stock (>0 if traded)
//    "cumulativeQuoteQty": "0",                               // Traded quantity in money (>0 if traded)
//    "cost": "0.05",                                          // Total amount
//    "side": "Bid",                                           // Bid for buying base asset, Ask for selling base asset. FYI: For BTC_USDT trading pair, BTC is the base asset
//    "pair": "TRX_USDT",                                      // Traded pair
//    "price": "0.01",                                         // Price of the trade
//    "status": "Open",                                        // The status of the order
//    "createdAt": "2023-07-11T07:04:20.131Z",                 // Date-time of order creation, UTC
//    "updatedAt": "2023-07-11T07:04:20.131Z"                  // Date-time of the last update of the order, UTC
//}

internal object OrderSchema : Table("dbo.active") {
    val id = varchar("id", 36)
    val type = varchar("type", 12)
    val quantity = double("quantity")
    val executedQuantity = double("executedQuantity")
    val cumulativeQuoteQty = double("cumulativeQuoteQty")
    val cost = double("cost")
    val side = varchar("side", 5)
    val pair = varchar("pair", 15)
    val price = double("price")
    val status = varchar("status", 12)
    val createdAt = varchar("createdAt", 25)
    val updatedAt = varchar("updatedAt", 25)
    val cancel = bool("cancel")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}