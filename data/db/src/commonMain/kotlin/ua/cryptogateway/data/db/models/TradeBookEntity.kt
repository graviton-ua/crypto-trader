package ua.cryptogateway.data.db.models


data class TradeBookEntity(
    val id: String,
    val pair: String,
    val quoteQuantity: Double,
    val matchPrice: Double,
    val matchQuantity: Double,
    val side: String,
    val createdAt: String
)
