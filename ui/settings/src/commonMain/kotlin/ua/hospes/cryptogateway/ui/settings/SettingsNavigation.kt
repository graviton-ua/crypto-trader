package ua.hospes.cryptogateway.ui.home

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