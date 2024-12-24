package ua.crypto.ui.configs

import androidx.compose.runtime.Immutable
import ua.crypto.domain.models.BotConfigModel

@Immutable
data class ConfigsViewState(
    val groups: Map<String, List<BotConfigModel>> = emptyMap(),
) {
    companion object {
        val Init = ConfigsViewState()
    }
}