package ua.crypto.core.connorsrsi

internal fun List<Double>.rs(lookBackPeriod: Int): List<Double> {
    // Ensure the input list is large enough for the calculation
    when {
        lookBackPeriod < 1 ->
            throw IllegalArgumentException("LookBackPeriod have to be bigger then 0")

        this.size < lookBackPeriod + 1 ->
            throw IllegalArgumentException("Not enough data points to calculate RS. Required: ${lookBackPeriod + 1}, Provided: ${this.size}")
    }

    // Allocate same size (as original) list with changes and prefilled 0.0 as initial values
    //val changes = MutableList<Double>(this.size) { 0.0 }
    // Allocate same size (as original) list with gains and prefilled 0.0 as initial values
    val gains = MutableList<Double>(this.size) { 0.0 }
    // Allocate same size (as original) list with losses and prefilled 0.0 as initial values
    val losses = MutableList<Double>(this.size) { 0.0 }
    // Allocate same size (as original) list with losses and prefilled 0.0 as initial values
    val rs = MutableList<Double>(this.size) { 0.0 }

    var previousAverageGain: Double? = null
    var previousAverageLoss: Double? = null

    // Loop and calculate changes/gains/losses
    for (i in 1 until this.size) {
        val change = this[i] - this[i - 1]
        //changes[i] = change
        gains[i] = if (change > 0) change else 0.0
        losses[i] = if (change < 0) -change else 0.0

        if (i >= lookBackPeriod) {
            val averageGain = previousAverageGain?.let { average(it, gains[i], lookBackPeriod) }
                ?: firstAverage(gains.take(i).sum(), gains[i], lookBackPeriod)
            val averageLoss = previousAverageLoss?.let { average(it, losses[i], lookBackPeriod) }
                ?: firstAverage(losses.take(i).sum(), losses[i], lookBackPeriod)

            rs[i] = averageGain / averageLoss

            previousAverageGain = averageGain
            previousAverageLoss = averageLoss
        }
    }

    return rs
}

fun List<Double>.rsi(lookBackPeriod: Int): List<Double> {
    return this.rs(lookBackPeriod).map { 100 - (100 / (1 + it)) }
}


private fun firstAverage(totalPrevious: Double, current: Double, lookBackPeriod: Int): Double {
    return (totalPrevious + current) / lookBackPeriod
}

private fun average(previousAverage: Double, current: Double, lookBackPeriod: Int): Double {
    return (previousAverage * (lookBackPeriod - 1) + current) / lookBackPeriod
}