package ua.cryptogateway.data.web.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//{
//    "type": "Limit",
//    "orderSide": "Bid",
//    "pair": "BTC_USDT",
//    "price": "26440.46",
//    "quantity": "0.06",   //"quantity": 0.06,
//}

@Serializable
data class CreateOrderRequest(
    val type: String,
    val orderSide: String,
    val pair: String,
    val price: Double,
    val quantity: Double,
)
