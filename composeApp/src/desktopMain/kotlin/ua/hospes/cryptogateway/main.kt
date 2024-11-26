package ua.hospes.cryptogateway

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.flow.collectLatest
import saschpe.log4k.Log
import ua.cryptogateway.inject.DesktopApplicationComponent
import ua.cryptogateway.inject.create

fun main() {
    System.setProperty("skiko.renderApi", "OPENGL") //TODO: Fixes issue with G-Sync stuttering

    val applicationComponent = DesktopApplicationComponent.create()
    applicationComponent.initializers.initialize()
    Log.debug { "Desktop main" }

    application {

        val observeFees = applicationComponent.observeMe
        LaunchedEffect(observeFees) {
            observeFees.flow.collectLatest {
                Log.debug(it.toString())
            }
        }
        LaunchedEffect(observeFees) {
            observeFees.invoke(Unit)
        }


        Window(
            onCloseRequest = ::exitApplication,
            title = "CryptoGateway",
        ) {
            App()
        }
    }
}