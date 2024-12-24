package ua.crypto.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    // For now we not gonna support dark theme
    val localAppColors: AppColorsScheme = if (darkTheme) AppDarkColorsScheme else AppLightColorsScheme

    CompositionLocalProvider(
        LocalAppColor provides localAppColors,
        LocalAppTypography provides AppTypography,
        LocalAppShapes provides AppShapes,
    ) {
        MaterialTheme(
            colorScheme = LocalAppColor.current.asMaterialColors(),
            //typography = LocalAppTypography.current.asMaterialTypography(),
            shapes = LocalAppShapes.current.asMaterialShapes(),
        ) {
            content()
        }
    }
}