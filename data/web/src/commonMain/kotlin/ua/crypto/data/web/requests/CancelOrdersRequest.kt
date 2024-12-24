package ua.crypto.data.web.requests

import kotlinx.serialization.Serializable

//{
//    "orderIds":
//    [
//        "{{OPEN_ORDER_ID_1}}",
//        "{{OPEN_ORDER_ID_2}}",
//        "{{OPEN_ORDER_ID_3}}",
//        "{{OPEN_ORDER_ID_4}}"
//    ]
//}

@Serializable
data class CancelOrdersRequest(
    val orderIds: List<String>,
)