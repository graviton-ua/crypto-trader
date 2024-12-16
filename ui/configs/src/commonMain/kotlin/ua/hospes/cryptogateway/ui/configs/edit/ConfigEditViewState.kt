package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import ua.cryptogateway.data.models.Order

@Immutable
data class ConfigEditViewState(
    val title: String? = null,

    val baseAsset: TextFieldValue = TextFieldValue(),
    val quoteAsset: TextFieldValue = TextFieldValue(),
    val side: Order.Side = Order.Side.Sell,
    val fond: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val startPrice: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val priceStep: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val biasPrice: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val minSize: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val orderSize: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val sizeStep: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val orderAmount: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val priceForce: Boolean = false,
    val market: Boolean = false,
    val basePrec: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val quotePrec: TextFieldValue = TextFieldValue(text = "0", selection = TextRange(1)),
    val active: Boolean = false,

    val deleteAvailable: Boolean = false,
) {
    companion object {
        val Init = ConfigEditViewState()
    }
}

@Immutable
sealed interface ConfigEditViewEvent {
    data object ConfigSaved : ConfigEditViewEvent
}