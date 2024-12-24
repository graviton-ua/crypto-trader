package ua.crypto.data.db.models

import kotlinx.datetime.Instant
import ua.crypto.data.models.Order

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

data class OrderEntity(
    val id: String,
    val type: Order.Type,
    val quantity: Double,
    val executedQuantity: Double,
    val cumulativeQuoteQty: Double,
    val cost: Double,
    val side: Order.Side,
    val pair: String,
    val price: Double,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val cancel: Boolean? = null,
) : Order