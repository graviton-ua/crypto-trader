package ua.crypto.core.exponentialma

import ua.crypto.core.connorsrsi.crsi


fun List<Double>.sma(numberOfPeriods: Int): List<Double> {
    if (this.size < numberOfPeriods) {
        throw IllegalArgumentException("The length of the array must be at least the period SMA. Required: ${numberOfPeriods}, Provided: ${this.size}")
    }

    val smaValues = MutableList<Double>(0) { 0.0 }

    for (i in numberOfPeriods - 1 until this.size) {
        val sum = this.subList(i + 1 - numberOfPeriods, i + 1).sum()
        smaValues.add(sum / numberOfPeriods)
    }

    for (i in this.indices){
        if (i>=4){
        println(
            "${"%12.2f".format(this[i])} | ${"%12.2f".format(smaValues[i-4])}"
        )}
        else println(
            "${"%12.2f".format(this[i])} | "
        )
    }

    return smaValues
}

