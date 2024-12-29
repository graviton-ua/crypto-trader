package ua.crypto.ui.services

import androidx.compose.runtime.Immutable
import ua.crypto.core.settings.TraderPreferences.LogLevel

@Immutable
data class ServicesViewState(
    val port: String = "",
    val logLevel: LogLevel = LogLevel.INFO,
) {


    companion object {
        val Init = ServicesViewState()
    }
}