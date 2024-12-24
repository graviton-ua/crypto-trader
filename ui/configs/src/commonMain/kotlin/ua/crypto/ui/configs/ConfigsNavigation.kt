package ua.crypto.ui.configs

import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import ua.crypto.ui.common.navigation.resultBackNavigator
import ua.crypto.ui.common.navigation.resultRecipient
import ua.crypto.ui.configs.edit.ConfigEditDialog

fun NavGraphBuilder.addConfigsScreen(
    diComponent: ConfigsComponent,
    navController: NavController,
) {
    composable<ConfigsScreen> {
        ConfigsScreen(
            diComponent = diComponent,
            navigateConfigEdit = { navController.navigate(ConfigEditDialog(id = it)) },
            resultConfigEdit = resultRecipient<ConfigEditDialog, Boolean>(it),
        )
    }
}

fun NavGraphBuilder.addConfigEditDialog(
    diComponent: ConfigsComponent,
    navController: NavController,
) {
    dialog<ConfigEditDialog>(
        typeMap = ConfigEditDialog.typeMap,
        dialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        ConfigEditDialog(
            diComponent = diComponent,
            savedStateHandle = it.savedStateHandle,
            navigateUp = navController::navigateUp,
            resultNavigator = resultBackNavigator<ConfigEditDialog, Boolean>(navController = navController),
        )
    }
}