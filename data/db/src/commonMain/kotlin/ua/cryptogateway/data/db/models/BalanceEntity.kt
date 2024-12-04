package ua.cryptogateway.data.db.models

import kotlinx.datetime.Instant

data class BalanceEntity(
    val currency: String,
    val balance: Double,
    val lockBalance: Double,
    val entire: Double,
    val timestamp: Instant,
)