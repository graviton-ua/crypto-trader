package ua.cryptogateway.data.db.models

import kotlinx.datetime.Instant

data class BalanceEntity(
    val currency: String,
    val entire: Double,
    val available: Double,
    val timestamp: Instant,
)