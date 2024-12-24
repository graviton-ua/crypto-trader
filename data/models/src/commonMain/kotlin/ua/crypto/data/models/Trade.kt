package ua.crypto.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Trade {

    @Serializable
    enum class Type {
        @SerialName("Sell") Sell,
        @SerialName("Buy") Buy;
    }
}