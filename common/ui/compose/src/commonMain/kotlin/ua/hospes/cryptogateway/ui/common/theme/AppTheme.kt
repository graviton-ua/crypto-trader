package ua.hospes.cryptogateway.ui.common.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppColor: ProvidableCompositionLocal<AppColorsScheme> =
    staticCompositionLocalOf { AppLightColorsScheme }

val LocalAppTypography: ProvidableCompositionLocal<AppTypography> =
    staticCompositionLocalOf { AppTypography }

val LocalAppShapes: ProvidableCompositionLocal<AppShapes> =
    staticCompositionLocalOf { AppShapes }

object AppTheme {
    val colors: AppColorsScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColor.current
    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current
}