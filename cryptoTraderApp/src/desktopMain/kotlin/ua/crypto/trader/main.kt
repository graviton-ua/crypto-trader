package ua.crypto.trader

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.cancel
import ua.crypto.shared.inject.DesktopApplicationComponent
import ua.crypto.shared.inject.WindowComponent
import ua.crypto.shared.inject.create
import java.util.*

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
    //Locale.setDefault(Locale("ru", "RU"))
    System.setProperty("skiko.renderApi", "OPENGL") //TODO: Fixes issue with G-Sync stuttering
    val applicationComponent = DesktopApplicationComponent.create()
    applicationComponent.initializers.initialize()

    application(exitProcessOnExit = false) {
        val initializationCompleted = remember { mutableStateOf(true) }

//        LaunchedEffect(applicationComponent) {
//            applicationComponent.suspendedInitializers.initialize()
//            initializationCompleted.value = true // Signal that initialization is complete
//        }

        val services = applicationComponent.traderServices
        if (initializationCompleted.value)  // Ensure DisposableEffect runs only after initialization
            DisposableEffect(services) {
                services.start()
                onDispose { services.stop() }
            }

        val appScope = applicationComponent.appScope
        DisposableEffect(appScope) {
            onDispose { appScope.cancel(null) }
        }

        Window(
            state = rememberWindowState(size = DpSize(1300.dp, 660.dp)),
            resizable = true,
            onCloseRequest = ::exitApplication,
            title = "CryptoTrader",
        ) {
            val uiComponent = remember(applicationComponent) { WindowComponent.create(applicationComponent) }

            App(
                uiComponent = uiComponent,
            )
        }
    }
}