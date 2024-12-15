package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import ua.cryptogateway.domain.models.BotConfigModel

@Immutable
data class ConfigEditViewState(
    val title: String? = null,

    val baseAsset: TextFieldValue = TextFieldValue(),
) {
    companion object {
        val Init = ConfigEditViewState()
    }
}