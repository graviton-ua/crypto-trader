package ua.cryptogateway.data.web.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//{
//    "currency": "UAH",                    // Wallet currency
//    "balance": "7134.6",                  // Available balance, precision depends on the currency
//    "lockBalance": "100"                  // Minimum amount locked on the balance
//},

@Serializable
data class KunaBalance(
    @SerialName("currency") val currency: String,
    @SerialName("balance") val balance: Double,
    @SerialName("lockBalance") val lockBalance: Double,
    val timestamp: Instant = Clock.System.now(),
) {
    val entire: Double = balance + lockBalance

//    val localTimeStamp: LocalDateTime
//        get() = timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
}