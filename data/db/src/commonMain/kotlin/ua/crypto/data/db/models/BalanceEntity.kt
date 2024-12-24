package ua.crypto.data.db.models

import kotlinx.datetime.*

data class BalanceEntity(
    val currency: String,
    val balance: Double,
    val lockBalance: Double,
    val entire: Double,
    val timestamp: Instant = Clock.System.now(),
) {
    val localTimeStamp: LocalDateTime
        get() = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
}