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

        val observeMe = applicationComponent.observeMe
        LaunchedEffect(observeMe) {
            observeMe.flow.collectLatest {
                Log.debug(it.toString())
            }
        }
        LaunchedEffect(observeMe) {
            observeMe.invoke(Unit)
        }

        val observeOrdersGrid = applicationComponent.observeOrdersGrid
        LaunchedEffect(observeOrdersGrid) {
            observeOrdersGrid.flow.collectLatest {
                Log.debug(it.toString())
            }
        }
        LaunchedEffect(observeOrdersGrid) {
            observeOrdersGrid.invoke(Unit)
        }


        Window(
            onCloseRequest = ::exitApplication,
            title = "CryptoGateway",
        ) {
            App()
        }
    }
}