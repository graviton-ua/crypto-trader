package ua.crypto.ui.settings

import androidx.compose.runtime.Immutable
import ua.crypto.core.settings.TraderPreferences.LogLevel

@Immutable
data class SettingsViewState(
    val port: String = "",
    val logLevel: LogLevel = LogLevel.INFO,

    val kunaApiKey: String = "",
) {


    companion object {
        val Init = SettingsViewState()
    }
}