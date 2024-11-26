package ua.hospes.cryptogateway

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ua.cryptogateway.inject.DesktopApplicationComponent
import ua.cryptogateway.inject.create

fun main() {
    System.setProperty("skiko.renderApi", "OPENGL") //TODO: Fixes issue with G-Sync stuttering

    application {

        val applicationComponent = remember {
            DesktopApplicationComponent.create()
        }

        LaunchedEffect(applicationComponent) {
            applicationComponent.initializers.initialize()
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "CryptoGateway",
        ) {
            App()
        }
    }
}