package ua.hospes.cryptogateway.ui.configs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.addConfigsScreen(
    diComponent: ConfigsComponent,
    navController: NavController,
) {
    composable<ConfigsScreen> {
        ConfigsScreen(
            diComponent = diComponent,
        )
    }
}