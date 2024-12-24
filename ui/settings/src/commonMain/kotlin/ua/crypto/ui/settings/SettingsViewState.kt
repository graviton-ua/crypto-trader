package ua.crypto.ui.settings

import androidx.compose.runtime.Immutable
import ua.crypto.core.settings.TiviPreferences.LogLevel

@Immutable
data class SettingsViewState(
    val port: String = "",
    val logLevel: LogLevel = LogLevel.INFO,
) {


    companion object {
        val Init = SettingsViewState()
    }
}