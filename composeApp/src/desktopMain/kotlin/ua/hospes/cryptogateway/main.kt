package ua.hospes.cryptogateway

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    System.setProperty("skiko.renderApi", "OPENGL") //TODO: Fixes issue with G-Sync stuttering
    Window(
        onCloseRequest = ::exitApplication,
        title = "CryptoGateway",
    ) {
        App()
    }
}