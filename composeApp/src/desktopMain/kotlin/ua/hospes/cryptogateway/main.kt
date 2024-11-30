package ua.hospes.cryptogateway

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ua.cryptogateway.inject.DesktopApplicationComponent
import ua.cryptogateway.inject.create

/**
 * Main entry point of the CryptoGateway desktop application.
 *
 * This function is responsible for setting up the rendering API, initializing application
 * components, and managing the main application window lifecycle.
 *
 * Key responsibilities:
 * - Sets the rendering API to OPENGL to mitigate issues with G-Sync stuttering.
 * - Initializes the `DesktopApplicationComponent` which acts as the main application component.
 * - Logs the start of the desktop application for debugging purposes.
 * - Launches and disposes background operations, including initialization and data pulling.
 * - Initializes and manages lifecycle for observable components and order data pulling.
 * - Constructs and displays the main application window with the user interface defined in `App` composable.
 */
fun main() {
    System.setProperty("skiko.renderApi", "OPENGL") //TODO: Fixes issue with G-Sync stuttering
    val applicationComponent = DesktopApplicationComponent.create()
    applicationComponent.initializers.initialize()

    application {
        LaunchedEffect(applicationComponent) {
            applicationComponent.suspendedInitializers.initialize()
        }

        val ordersPuller = applicationComponent.ordersGridPuller
        DisposableEffect(ordersPuller) {
            ordersPuller.start()
            onDispose { ordersPuller.stop() }
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "CryptoGateway",
        ) {
            App()
        }
    }
}