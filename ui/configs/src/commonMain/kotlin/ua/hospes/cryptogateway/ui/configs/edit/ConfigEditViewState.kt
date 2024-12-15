package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.runtime.Immutable
import ua.cryptogateway.domain.models.BotConfigModel

@Immutable
data class ConfigEditViewState(
    val groups: Map<String, List<BotConfigModel>> = emptyMap(),
) {
    companion object {
        val Init = ConfigEditViewState()
    }
}