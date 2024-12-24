package ua.crypto.data.db.models

data class OrderBookEntity(
    val pair: String,
    val asks: List<List<Double>>,
    val bids: List<List<Double>>,
)