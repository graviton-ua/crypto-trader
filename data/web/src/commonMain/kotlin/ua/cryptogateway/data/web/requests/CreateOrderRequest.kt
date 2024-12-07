package ua.cryptogateway.data.web.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// Body to create a Market order to buy BTC using "quantity"
//{
//    "type": "Market",
//    "orderSide": "Bid",
//    "pair": "BTC_USDT",
//    "quantity": "0.06"
//} // I want to buy 0.06 BTC

@Serializable
data class CreateOrderRequest(
    @SerialName("type") val type: String,
    @SerialName("orderSide") val orderSide: String,
    @SerialName("pair") val pair: String,
    @SerialName("quantity") val quantity: Double,
)
