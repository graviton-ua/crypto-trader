package ua.crypto.core.connorsrsi

import kotlin.time.measureTimedValue

fun List<Double>.crsi(close: Int, streak: Int, percentRank: Int): List<Double> {
    // RSI(Close,3)
    val rsiCloseResult = measureTimedValue { this.rsi(close) }.also { println("RSI Close Exec time: ${it.duration}") }.value
//    for (i in rsiCloseResult.indices) {
//        println("${"%12.2f".format(this[i])} | ${"%12.2f".format(rsiCloseResult[i])}")
//    }

    // RSI(Streak,2)
    val streaksResult = measureTimedValue { this.streaks() }.also { println("UpDown Exec time: ${it.duration}") }.value
    val rsiStreakResult = measureTimedValue { streaksResult.rsi(streak) }.also { println("RSI Streak Exec time: ${it.duration}") }.value
//    for (i in rsiStreakResult.indices) {
//        println("${"%12.2f".format(this[i])} | ${"%12.2f".format(streaksResult[i])} | ${"%12.2f".format(rsiStreakResult[i])}")
//    }

    // PercentRank(10)
    val percentRankResult = measureTimedValue { this.percentRank(percentRank) }.also { println("Percent Rank Exec time: ${it.duration}") }.value
//    for (i in percentRankResult.indices) {
//        println("${"%12.2f".format(this[i])} | ${"%12.2f".format(rsiCloseResult[i])}")
//    }

    // Allocate same size (as original) list with CRSI and prefilled 0.0 as initial values
    val crsi = MutableList<Double>(this.size) { 0.0 }
    println(
        "${"%6s".format("index")} | ${"%12s".format("price")} | " +
                "${"%12s".format("RSI(close)")} | " +
                "${"%12s".format("RSI(streak)")} | " +
                "${"%12s".format("PercentRank")} | " +
                "%12s".format("CRSI")
    )
    for (i in this.indices) {
        crsi[i] = (rsiCloseResult[i] + rsiStreakResult[i] + percentRankResult[i]) / 3
        println(
            "${"%6d".format(i)} | ${"%12.2f".format(this[i])} | " +
                    "${"%12.2f".format(rsiCloseResult[i])} | " +
                    "${"%12.2f".format(rsiStreakResult[i])} | " +
                    "${"%12.2f".format(percentRankResult[i])} | " +
                    "%12.2f".format(crsi[i])
        )
    }

    return crsi
}