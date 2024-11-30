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
    @SerialName("entire") val entire: Double = 0.0, //TODO: What is entire ?!?!
    @SerialName("balance") val available: Double,
    val timestamp: Instant = Clock.System.now(),
)