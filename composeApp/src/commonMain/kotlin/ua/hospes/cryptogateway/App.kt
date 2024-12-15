package ua.hospes.cryptogateway

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ua.cryptogateway.inject.SharedUiComponent
import ua.hospes.cryptogateway.ui.common.theme.AppTheme
import ua.hospes.cryptogateway.ui.configs.ConfigsScreen
import ua.hospes.cryptogateway.ui.configs.addConfigsScreen
import ua.hospes.cryptogateway.ui.home.HomeScreen
import ua.hospes.cryptogateway.ui.home.addHomeScreen
import ua.hospes.cryptogateway.ui.settings.SettingsScreen
import ua.hospes.cryptogateway.ui.settings.addSettingsScreen

@Composable
fun App(
    uiComponent: SharedUiComponent,
) {
    AppTheme {
        val destinations = remember { listOf(HomeScreen, ConfigsScreen, SettingsScreen) }
        val navController = rememberNavController()
        Row {
            NavigationRail {
                destinations.forEach {
                    NavigationRailItem(
                        icon = {
                            Icon(
                                imageVector = when (it) {
                                    HomeScreen -> Icons.Default.Home
                                    ConfigsScreen -> Icons.Default.SmartToy
                                    SettingsScreen -> Icons.Default.Settings
                                    else -> Icons.Default.QuestionMark
                                },
                                contentDescription = null,
                            )
                        },
                        selected = navController.currentBackStackEntryAsState().value?.destination?.hasRoute(it::class) == true,
                        onClick = {
                            navController.navigate(it) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }

            NavHost(
                navController = navController,
                startDestination = HomeScreen,
                modifier = Modifier.weight(1f),
            ) {
                addHomeScreen(diComponent = uiComponent, navController = navController)
                addConfigsScreen(diComponent = uiComponent, navController = navController)
                addSettingsScreen(diComponent = uiComponent, navController = navController)
            }
        }
    }
}