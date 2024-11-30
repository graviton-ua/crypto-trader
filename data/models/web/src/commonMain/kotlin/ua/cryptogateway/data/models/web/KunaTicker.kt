package ua.cryptogateway.data.models.web

import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//{
//    "pair": "BTC_USDT",                                   // Traded pair
//    "percentagePriceChange": "-0.03490931899641581",      // Relative price change, in percent
//    "price": "27900",                                     // Current median price
//    "equivalentPrice": "",                                // TBD
//    "high": "29059.69",                                   // Highest price
//    "low": "27900",                                       // Lowest price
//    "baseVolume": "2.9008499999999993",                   // Traded volume as base
//    "quoteVolume": "82251.41477976",                      // Traded volume as quote
//    "bestBidPrice": "27926.91",                           // The best bid price now
//    "bestAskPrice": "27970.02",                           // The best ask price now
//    "priceChange": "-973.9700000000012"                   // Absolute price change
//}

@Serializable
data class KunaTicker(
    @SerialName("pair") val pairName: String,
    @SerialName("high") val priceHigh: Double,
    @SerialName("bestAskPrice") val priceAsk: Double,
    @SerialName("bestBidPrice") val priceBid: Double,
    @SerialName("low") val priceLow: Double,
    @SerialName("price") val priceLast: Double,
    @SerialName("priceChange") val change: Double,
    val timestamp: Instant = System.now(),
)