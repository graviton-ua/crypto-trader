package ua.crypto.ui.common

fun Double.formatDouble(maxDecimals: Int = 8): String {
    val format = "%.${maxDecimals}f"
    return String.format(format, this)
        .replace(",", ".")
        .trimEnd('0') // Remove trailing zeros
        .trimEnd('.') // Remove the dot if it's left dangling
}