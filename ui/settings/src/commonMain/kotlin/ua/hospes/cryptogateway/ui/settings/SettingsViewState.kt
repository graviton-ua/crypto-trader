package ua.hospes.cryptogateway.ui.settings

import androidx.compose.runtime.Immutable
import ua.cryptogateway.settings.TiviPreferences.LogLevel

@Immutable
data class SettingsViewState(
    val port: String = "",
    val logLevel: LogLevel = LogLevel.INFO,
) {


    companion object {
        val Init = SettingsViewState()
    }
}