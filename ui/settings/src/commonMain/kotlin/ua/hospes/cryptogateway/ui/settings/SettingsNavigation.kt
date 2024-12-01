package ua.hospes.cryptogateway.ui.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.addSettingsScreen(
    navController: NavController,
) {
    composable<SettingsScreen> {
        SettingsScreen()
    }
}