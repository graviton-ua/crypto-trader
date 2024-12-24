package ua.crypto.core.animations

fun lerp(
    startValue: Float,
    endValue: Float,
    fraction: Float,
) = startValue + fraction * (endValue - startValue)