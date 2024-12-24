package ua.crypto.data.db.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class OhlcvEntity(
    val id: Int,

    val pair: String,

    val openTime: Instant,     // open time
    val closeTime: Instant,    // close time
    val trades: Int,           // trades
    val openPrice: Double,     // open price
    val highPrice: Double,     // high price
    val lowPrice: Double,      // low price
    val closePrice: Double,    // close price
    val volume: Double,        // volume

    val timestamp: Instant = Clock.System.now()
)