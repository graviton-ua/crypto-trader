package ua.cryptogateway.data.web.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//{
//    "id": "3e5591ba-2778-4d85-8851-54284045ea44",       // Unique identifier of a trade
//    "pair": "BTC_USDT",                                 // Market pair that is being traded
//    "quoteQuantity": "11528.8118",                      // Qty of the quote asset, USDT in this example
//    "matchPrice": "18649",                              // Exchange price at the moment of execution
//    "matchQuantity": "0.6182",                          // Qty of the base asset, BTC in this example
//    "createdAt": "2022-09-23T14:30:41.486Z",            // Date-time of trade execution, UTC
//    "side": "Ask"                                       // Trade type: `Ask` or `Bid`. Bid for buying base asset, Ask for selling base asset (e.g. for BTC_USDT trading pair, BTC is the base asset).
//}
@Serializable
data class KunaTradesBook(
    @SerialName("id") val id: String,
    @SerialName("pair") val pair: String,
    @SerialName("quoteQuantity") val quoteQuantity: Double,
    @SerialName("matchPrice") val matchPrice: Double,
    @SerialName("matchQuantity") val matchQuantity: Double,
    @SerialName("side") val side: String,
    @SerialName("createdAt") val createdAt: Instant
)
