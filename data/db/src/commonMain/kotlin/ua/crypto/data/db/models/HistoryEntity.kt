package ua.crypto.data.db.models

import kotlinx.datetime.Instant

data class HistoryEntity(
    val id: String,
    val orderId:String,
    val pair:String,
    val quantity:Double,
    val price:Double,
    val isTaker:Boolean,
    val fee:Double,
    val feeCurrency:String,
    val isBuyer:Boolean,
    val quoteQuantity:Double,
    val createdAt: Instant
)
