package ua.cryptogateway.connorsrsi

// ROC
// Calculates the percentage of change (rate of change) between the current value of source and its value length bars ago.
// It is calculated by the formula: 100 * change(src, length) / src[length].
// One-Day return (from book)
private fun List<Double>.roc(length: Int = 1): List<Double> {
    // Allocate same size (as original) list with one-day returns and prefilled 0.0 as initial values
    val returns = MutableList<Double>(this.size) { 0.0 }

    for (i in length until this.size) {
        val change = this[i] - this[i - length]
        returns[i] = (change / this[i - length]) * 100
    }

    return returns
}

fun List<Double>.percentRank(lookBackPeriod: Int): List<Double> {
    // Ensure the input list is large enough for the calculation
    when {
        lookBackPeriod < 1 ->
            throw IllegalArgumentException("LookBackPeriod have to be bigger then 0")

        this.size < lookBackPeriod + 1 ->
            throw IllegalArgumentException("Not enough data points to calculate RS. Required: ${lookBackPeriod + 1}, Provided: ${this.size}")
    }

    val roc = this.roc()
    // Allocate same size (as original) list with percent ranks and prefilled 0.0 as initial values
    val ranks = MutableList<Double>(this.size) { 0.0 }

    for (i in lookBackPeriod until this.size) {
        val oneDayReturn = roc[i]
        val many = roc.subList(i - lookBackPeriod, i).filter { it < oneDayReturn }.size.toDouble()
        ranks[i] = (many / lookBackPeriod) * 100
    }

    return ranks
}