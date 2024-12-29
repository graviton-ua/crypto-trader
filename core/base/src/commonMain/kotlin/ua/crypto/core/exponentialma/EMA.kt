package ua.crypto.core.exponentialma

fun List<Double>.rs(lookBackPeriod: Int): List<Double> {
    // Ensure the input list is large enough for the calculation
    when {
        lookBackPeriod < 1 ->
            throw IllegalArgumentException("LookBackPeriod have to be bigger then 0")

        this.size < lookBackPeriod ->
            throw IllegalArgumentException("Not enough data points to calculate EMA. Required: ${lookBackPeriod}, Provided: ${this.size}")
    }

    val ema = MutableList<Double>(this.size) { 0.0 }

    return ema
}