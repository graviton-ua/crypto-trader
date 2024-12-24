package ua.crypto.data.db.models

import ua.crypto.data.models.Order

data class BotConfigEntity(
    val id: Int,

    val baseAsset: String,
    val quoteAsset: String,

    val side: Order.Side,

    val fond: Double,

    val startPrice: Double,
    val priceStep: Double,
    val biasPrice: Double,
    val minSize: Double,
    val orderSize: Int,
    val sizeStep: Double,
    val orderAmount: Int,
    val priceForce: Boolean,
    val market: Boolean,

    val basePrec: Int,
    val quotePrec: Int,
    val active: Boolean,
) {
    val pair: String = "${baseAsset}_${quoteAsset}"
}