package ua.cryptogateway.connorsrsi

fun List<Double>.streaks(): List<Double> {
    // Allocate same size (as original) list with streaks and prefilled 0.0 as initial values
    val streaks = MutableList<Double>(this.size) { 0.0 }

    for (i in 1 until this.size) {
        val change = this[i] - this[i - 1]
        streaks[i] = when {
            change > 0 -> when {
                streaks[i - 1] >= 0 -> streaks[i - 1] + 1
                else -> 1.0
            }

            change < 0 -> when {
                streaks[i - 1] <= 0 -> streaks[i - 1] - 1
                else -> -1.0
            }

            else -> 0.0
        }
    }

    return streaks
}