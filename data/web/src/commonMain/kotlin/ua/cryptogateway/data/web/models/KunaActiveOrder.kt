package ua.cryptogateway.data.web.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class KunaActiveOrder(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String,
    @SerialName("quantity") val quantity: Double,
    @SerialName("executedQuantity") val executedQuantity: Double,
    @SerialName("cumulativeQuoteQty") val cumulativeQuoteQty: Double,
    @SerialName("cost") val cost: Double,
    @SerialName("side") val side: String,
    @SerialName("pair") val pair: String,
    @SerialName("price") val price: Double,
    @SerialName("status") val status: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updatedAt") val updatedAt: String,
    val timestamp: Instant = Clock.System.now(),
)