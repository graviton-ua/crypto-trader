package ua.cryptogateway.data.web.models

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*


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
    @SerialName("asks") val asks: List<Order>,
    @SerialName("bids") val bids: List<Order>,
) {
    @Serializable(with = KunaOrderBookOrderSerializer::class)
    data class Order(
        val price: Double,
        val quantity: Double,
    )
}


internal object KunaOrderBookOrderSerializer : KSerializer<KunaOrderBook.Order> {
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("KunaOrderBook.Order") {
        element<Double>("price")
        element<Double>("quantity")
    }


    override fun deserialize(decoder: Decoder): KunaOrderBook.Order {
        val input = decoder as? JsonDecoder
            ?: throw SerializationException("This class can be loaded only by Json")

        // Decode the array
        val jsonArray = input.decodeJsonElement().jsonArray

        if (jsonArray.size != 2) {
            throw SerializationException("Expected a JSON array with 2 elements, but got ${jsonArray.size}")
        }

        val price = jsonArray[0].jsonPrimitive.double
        val quantity = jsonArray[1].jsonPrimitive.double

        return KunaOrderBook.Order(price, quantity)
    }


    override fun serialize(encoder: Encoder, value: KunaOrderBook.Order) {
        val output = encoder as? JsonEncoder
            ?: throw SerializationException("This class can be saved only by Json")

        val jsonArray = JsonArray(
            listOf(
                JsonPrimitive(value.price),
                JsonPrimitive(value.quantity)
            )
        )
        output.encodeJsonElement(jsonArray)
    }
}
