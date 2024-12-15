package ua.hospes.cryptogateway.ui.common.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

interface AppColorsScheme {
    val primary: Color
    val primaryVariant: Color
    val primarySurface: Color
    val secondary: Color
    val secondaryVariant: Color
    val secondarySurface: Color
    val tertiary: Color
    val tertiaryVariant: Color
    val tertiarySurface: Color
    val background: Color
    val surface: Color
    val error: Color
    val onPrimary: Color
    val onPrimarySurface: Color
    val onSecondary: Color
    val onSecondarySurface: Color
    val onTertiary: Color
    val onTertiarySurface: Color
    val onBackground: Color
    val onSurface: Color
    val onError: Color

    val even: Color
    val odd: Color
    val link: Color

    val isLight: Boolean

    fun asMaterialColors(): ColorScheme = lightColorScheme(
        primary = primary,
        secondary = secondary,
        background = background,
        surface = surface,
        error = error,
        onPrimary = onPrimary,
        onSecondary = onSecondary,
        onBackground = onBackground,
        onSurface = onSurface,
        onError = onError,
    )
}


// Light theme
object AppLightColorsScheme : AppColorsScheme {
    override val primary: Color = AppColorPalette.Brand500
    override val primaryVariant: Color = primary
    override val primarySurface: Color = AppColorPalette.Brand50
    override val secondary: Color = AppColorPalette.Attention500
    override val secondaryVariant: Color = secondary
    override val secondarySurface: Color = AppColorPalette.Attention50
    override val tertiary: Color = AppColorPalette.Curious500
    override val tertiaryVariant: Color = tertiary
    override val tertiarySurface: Color = AppColorPalette.Curious50
    override val background: Color = AppColorPalette.White
    override val surface: Color = background
    override val error: Color = AppColorPalette.AlertRed
    override val onPrimary: Color = AppColorPalette.White
    override val onPrimarySurface: Color = AppColorPalette.Black
    override val onSecondary: Color = AppColorPalette.White
    override val onSecondarySurface: Color = AppColorPalette.Black
    override val onTertiary: Color = AppColorPalette.White
    override val onTertiarySurface: Color = AppColorPalette.Black
    override val onBackground: Color = AppColorPalette.Black
    override val onSurface: Color = onBackground
    override val onError: Color = AppColorPalette.White
    override val even: Color = Color.Transparent
    override val odd: Color = AppColorPalette.Grey50
    override val link: Color = primary
    override val isLight: Boolean = true
}

// Dark theme
object AppDarkColorsScheme : AppColorsScheme {
    override val primary: Color = AppColorPalette.Brand500
    override val primaryVariant: Color = primary
    override val primarySurface: Color = AppColorPalette.Brand50
    override val secondary: Color = AppColorPalette.Attention500
    override val secondaryVariant: Color = secondary
    override val secondarySurface: Color = AppColorPalette.Attention50
    override val tertiary: Color = AppColorPalette.Curious500
    override val tertiaryVariant: Color = tertiary
    override val tertiarySurface: Color = AppColorPalette.Curious50
    override val background: Color = AppColorPalette.White
    override val surface: Color = background
    override val error: Color = AppColorPalette.AlertRed
    override val onPrimary: Color = AppColorPalette.White
    override val onPrimarySurface: Color = AppColorPalette.Black
    override val onSecondary: Color = AppColorPalette.White
    override val onSecondarySurface: Color = AppColorPalette.Black
    override val onTertiary: Color = AppColorPalette.White
    override val onTertiarySurface: Color = AppColorPalette.Black
    override val onBackground: Color = AppColorPalette.Black
    override val onSurface: Color = onBackground
    override val onError: Color = AppColorPalette.White
    override val even: Color = Color.Transparent
    override val odd: Color = AppColorPalette.Grey50
    override val link: Color = primary
    override val isLight: Boolean = true
}