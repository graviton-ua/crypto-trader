package ua.hospes.cryptogateway.ui.configs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import ua.hospes.cryptogateway.ui.common.navigation.resultBackNavigator
import ua.hospes.cryptogateway.ui.common.navigation.resultRecipient
import ua.hospes.cryptogateway.ui.configs.edit.ConfigEditDialog

fun NavGraphBuilder.addConfigsScreen(
    diComponent: ConfigsComponent,
    navController: NavController,
) {
    composable<ConfigsScreen> {
        ConfigsScreen(
            diComponent = diComponent,
            resultConfigEdit = resultRecipient<ConfigEditDialog, Unit>(it),
        )
    }
}

fun NavGraphBuilder.addConfigEditDialog(
    diComponent: ConfigsComponent,
    navController: NavController,
) {
    dialog<ConfigEditDialog> {
        ConfigEditDialog(
            diComponent = diComponent,
            resultNavigator = resultBackNavigator<ConfigEditDialog, Unit>(navController = navController),
        )
    }
}