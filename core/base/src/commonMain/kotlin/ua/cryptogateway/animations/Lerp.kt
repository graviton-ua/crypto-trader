package ua.cryptogateway.animations

fun lerp(
    startValue: Float,
    endValue: Float,
    fraction: Float,
) = startValue + fraction * (endValue - startValue)