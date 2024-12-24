package ua.crypto.data.web.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//[
//{
//    "id": "3e5591ba-2778-4d85-8851-54284045ea44",   // Unique identifier of a canceled order
//    "success": true                                 // Status for this order
//},
//{
//    "id": "4po3091bc-p278-o286-1452-o40s4045k42p",  // Unique identifier of a canceled order
//    "success": true                                 // Status for this order
//}
//]

@Serializable
data class KunaCancelledOrder(
    @SerialName("orderId") val id: String,
    val success: Boolean,
)