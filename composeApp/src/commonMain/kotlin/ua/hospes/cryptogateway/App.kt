package ua.hospes.cryptogateway

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import ua.cryptogateway.inject.SharedUiComponent
import ua.hospes.cryptogateway.ui.home.HomeScreen
import ua.hospes.cryptogateway.ui.home.addHomeScreen

@Composable
fun App(
    uiComponent: SharedUiComponent,
) {
    MaterialTheme {

        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = HomeScreen,
        ) {
            addHomeScreen(diComponent = uiComponent, navController = navController)
        }
    }
}