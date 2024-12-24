package ua.crypto.sync

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.application
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.jetbrains.compose.resources.painterResource
import ua.cryptogateway.inject.DesktopApplicationComponent
import ua.cryptogateway.inject.create
import ua.crypto.sync.resources.Res
import ua.crypto.sync.resources.letter_d
import java.awt.*
import javax.swing.SwingUtilities

fun main() {
    System.setProperty("skiko.renderApi", "OPENGL") //TODO: Fixes issue with G-Sync stuttering
    val applicationComponent = DesktopApplicationComponent.create()
    applicationComponent.initializers.initialize()

    application(exitProcessOnExit = true) {
        val initializationCompleted = remember { mutableStateOf(true) }

//        LaunchedEffect(applicationComponent) {
//            applicationComponent.suspendedInitializers.initialize()
//            initializationCompleted.value = true // Signal that initialization is complete
//        }

        val services = applicationComponent.services
        if (initializationCompleted.value)  // Ensure DisposableEffect runs only after initialization
            DisposableEffect(services) {
                services.start()
                onDispose { services.stop() }
            }

        val appScope = applicationComponent.appScope
        DisposableEffect(appScope) {
            onDispose { appScope.cancel(null) }
        }

        val icon = painterResource(Res.drawable.letter_d)
        val iconAwtImage = icon.toAwtImage(density = LocalDensity.current, layoutDirection = LayoutDirection.Ltr)
        SwingUtilities.invokeLater {
            addTrayIcon(icon = iconAwtImage) { exitApplication() }
        }

        // Keep the application running indefinitely
        println("Application is running. Press Ctrl+C to exit.")
        LaunchedEffect(Unit) {
            appScope.coroutineContext[Job]?.join() // Wait for app scope cancellation
        }
    }
}

fun addTrayIcon(icon: Image, onExit: () -> Unit) {
    val tray = SystemTray.getSystemTray()

    // Create the tray icon
    //val icon = Toolkit.getDefaultToolkit().getImage("path/to/icon.png") // Replace with your icon path
    val trayIcon = TrayIcon(icon, "CryptoData")
    trayIcon.isImageAutoSize = true

    // Create a popup menu
    val popupMenu = PopupMenu()

    // Add Exit menu item
    val exitItem = MenuItem("Exit")
    exitItem.addActionListener {
        onExit()
    }
    popupMenu.add(exitItem)

    // Attach menu to the tray icon
    trayIcon.popupMenu = popupMenu

    try {
        tray.add(trayIcon)
    } catch (e: AWTException) {
        println("Unable to add tray icon: ${e.message}")
    }
}

//fun main() {
//    println("Start app")
//    val applicationComponent = DesktopApplicationComponent.create()
//    applicationComponent.initializers.initialize()
//    println("AppInitializers initialized")
//
//    val scope = applicationComponent.appScope
//    println("DI scope")
//
//    scope.launch {
//        val services = applicationComponent.services
//        println("DI services")
//        services.start()
//        println("Services started")
//    }
//
//    // Setup shutdown hook for graceful termination
//    Runtime.getRuntime().addShutdownHook(Thread {
//        println("Shutdown signal received. Cleaning up...")
//        runBlocking {
//            //services.stop()
//            //println("Services stopped")
//            // Cancel the entire scope, even with SupervisorJob
//            scope.cancel()
//            scope.coroutineContext[Job]?.join() // Wait for all coroutines to finish
//        }
//        println("All tasks completed. Exiting application.")
//    })
//
//    // Block the main thread to keep the application running
//    println("Application started. Press Ctrl+C to exit.")
//    runBlocking {
//        try {
//            scope.coroutineContext[Job]?.join() // Wait for all child coroutines to complete
//        } catch (e: CancellationException) {
//            println("Main coroutine was cancelled.")
//        }
//    }
//
//    println("Application terminated.")
//    exitProcess(0)
//}