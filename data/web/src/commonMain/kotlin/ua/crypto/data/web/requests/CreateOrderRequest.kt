package ua.crypto.data.web.requests

import kotlinx.serialization.Serializable
import ua.crypto.data.models.Order


//{
//    "type": "Limit",
//    "orderSide": "Bid",
//    "pair": "BTC_USDT",
//    "price": "26440.46",
//    "quantity": "0.06",   //"quantity": 0.06,
//}

@Serializable
data class CreateOrderRequest(
    val type: Order.Type,
    val orderSide: Order.Side,
    val pair: String,
    val price: String? = null,
    val quantity: String,
) {
    constructor(
        type: Order.Type,
        orderSide: Order.Side,
        pair: String,
        price: Double? = null,
        quantity: Double,
    ) : this(
        type = type,
        orderSide = orderSide,
        pair = pair,
        price = price?.let { String.format("%.6f", it) },
        quantity = quantity.let { String.format("%.6f", it) },
    )
}