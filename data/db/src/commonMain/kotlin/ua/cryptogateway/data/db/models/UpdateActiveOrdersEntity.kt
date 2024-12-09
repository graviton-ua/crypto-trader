package ua.cryptogateway.data.db.models

import kotlinx.datetime.Instant

data class UpdateActiveOrdersEntity(
    val pairName: String,
    val priceAsk: Double,
    val priceBid: Double,
    val change: Double,
)
