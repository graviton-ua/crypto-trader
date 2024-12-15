package ua.hospes.cryptogateway.ui.configs

import androidx.compose.runtime.Immutable
import ua.cryptogateway.domain.models.BotConfigModel

@Immutable
data class ConfigsViewState(
    val groups: Map<String, List<BotConfigModel>> = emptyMap(),
) {
    companion object {
        val Init = ConfigsViewState()
    }
}