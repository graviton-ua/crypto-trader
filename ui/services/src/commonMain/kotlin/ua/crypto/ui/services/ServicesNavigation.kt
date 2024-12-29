package ua.crypto.ui.services

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.addServicesScreen(
    diComponent: ServicesComponent,
    navController: NavController,
) {
    composable<ServicesScreen> {
        ServicesScreen(
            diComponent = diComponent,
        )
    }
}