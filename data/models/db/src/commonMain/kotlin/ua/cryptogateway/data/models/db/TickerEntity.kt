package ua.cryptogateway.data.models.db

import kotlinx.datetime.Instant

data class TickerEntity(
    val pairName: String,
    val priceHigh: Double,
    val priceAsk: Double,
    val priceBid: Double,
    val priceLow: Double,
    val priceLast: Double,
    val change: Double,
    val timestamp: Instant,
)