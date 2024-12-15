package ua.cryptogateway.data.models

enum class Side(val code: String) {
    Sell("Ask"),
    Buy("Bid");

    companion object {
        fun fromKunaString(code: String): Side = when (code) {
            "Ask" -> Sell
            "Bid" -> Buy
            else -> Sell
        }
    }
}