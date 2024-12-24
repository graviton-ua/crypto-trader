package ua.crypto.data.web.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//[
//  {
//    id: "edb17459-c9bf-4148-9ae6-7367d7f55d71",        // Unique identifier of a trade
//    orderId: "a80bec3f-4ffa-45c1-9d78-f6301e9748fe",   // Unique identifier of an order associated with the trade
//    pair: "BTC_USDT",                                  // Traded pair, base asset first, followed by quoted asset
//    quantity: "1.5862",                                // Traded quantity of base asset
//    price: "19087",                                    // Price of the trade
//    isTaker: true,                                     // Various fees for Makers and Takers; "Market" orders are always `true`
//    fee: "0.0039655",                                  // Exchange commission fee
//    feeCurrency: "BTC",                                // Currency of the commission
//    isBuyer: true,                                     // Buy or sell the base asset
//    quoteQuantity: "30275.7994",                       // Quote asset quantity spent to fulfill the base amount
//    createdAt: "2022-09-29T13:43:53.824Z",             // Date-time of trade execution, UTC
//  },
//]

@Serializable
data class KunaHistory(
    @SerialName("id") val id: String,
    @SerialName("orderId") val orderId: String,
    @SerialName("pair") val pair: String,
    @SerialName("quantity") val quantity: Double,
    @SerialName("price") val price: Double,
    @SerialName("isTaker") val isTaker: Boolean,
    @SerialName("fee") val fee: Double,
    @SerialName("feeCurrency") val feeCurrency: String,
    @SerialName("isBuyer") val isBuyer: Boolean,
    @SerialName("quoteQuantity") val quoteQuantity: Double,
    @SerialName("createdAt") val createdAt: Instant
)
