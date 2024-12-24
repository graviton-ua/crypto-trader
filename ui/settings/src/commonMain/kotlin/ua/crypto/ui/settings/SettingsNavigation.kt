package ua.crypto.ui.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.addSettingsScreen(
    diComponent: SettingsComponent,
    navController: NavController,
) {
    composable<SettingsScreen> {
        SettingsScreen(
            diComponent = diComponent,
        )
    }
}