package ua.hospes.cryptogateway.ui.common.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object AppShapes {
    val extraSmall: RoundedCornerShape = RoundedCornerShape(2.dp)
    val small: RoundedCornerShape = RoundedCornerShape(4.dp)
    val medium: RoundedCornerShape = RoundedCornerShape(8.dp)
    val large: RoundedCornerShape = RoundedCornerShape(12.dp)
    val extraLarge: RoundedCornerShape = RoundedCornerShape(16.dp)

    fun asMaterialShapes(): Shapes = Shapes(
        extraSmall = extraSmall,
        small = small,
        medium = medium,
        large = large,
        extraLarge = extraLarge,
    )
}