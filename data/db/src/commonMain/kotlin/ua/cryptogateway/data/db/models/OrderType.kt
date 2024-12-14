package ua.cryptogateway.data.db.models

enum class OrderType(val code: String) {
    Limit("Limit"),
    Market("Market"),
    StopLossLimit("StopLossLimit"),
    TakeProfitLimit("TakeProfitLimit");

    companion object {
        fun fromKunaString(code: String): OrderType = when (code) {
            "Limit" -> Limit
            "Market" -> Market
            "StopLossLimit" -> StopLossLimit
            "TakeProfitLimit" -> TakeProfitLimit
            else -> Limit
        }
    }
}