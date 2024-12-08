package ua.cryptogateway.data.web.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//{
//    "asks": [        // An array of sell orders
//    [
//        "16950",     // Sell price, level 1
//        "0.001"      // Sell quantity, level 1
//    ],
//    [
//        "17000",     // Sell price, level 2
//        "0.01"       // Sell quantity, level 2
//    ]
//    ],
//    "bids": [        // An array of buy orders
//    [
//        "16700",     // Sell price, level 1
//        "0.01"       // Sell quantity, level 1
//    ],
//    [
//        "16000",     // Sell price, level 2
//        "0.001"      // Sell quantity, level 2
//    ]
//    ]
//}

@Serializable
data class KunaOrderBook(
    @SerialName("asks") val asks: List<List<Double>>,
    @SerialName("bids") val bids: List<List<Double>>,
) {
    @Serializable
    data class Order(
        val price: Double,
        val quantity: Double,
    )
}
