package ua.cryptogateway.data.db.models

import ua.cryptogateway.data.models.Order

data class BotConfigEntity(
    val baseAsset: String,
    val quoteAsset: String,

    val side: Order.Side,

    val fond: Double,

    val startPrice: Double,
    val priceStep: Double,
    val minSize: Double,
    val orderSize: Int,
    val orderAmount: Int,
    val priceForce: Boolean,
    val market: Boolean,

    val basePrec: Int,
    val quotePrec: Int,
    val active: Boolean,
)