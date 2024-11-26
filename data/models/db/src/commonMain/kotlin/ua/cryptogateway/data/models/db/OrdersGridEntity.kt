package ua.cryptogateway.data.models.db

import kotlinx.serialization.Serializable

@Serializable
data class OrdersGridEntity(val pairname: String?, val side: String?)