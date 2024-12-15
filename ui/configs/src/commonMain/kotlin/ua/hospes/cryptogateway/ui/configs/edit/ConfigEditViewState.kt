package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import ua.cryptogateway.data.models.Order

@Immutable
data class ConfigEditViewState(
    val title: String? = null,

    val baseAsset: TextFieldValue = TextFieldValue(),
    val quoteAsset: TextFieldValue = TextFieldValue(),
    val side: Order.Side = Order.Side.Sell,
    val fond: TextFieldValue = TextFieldValue(),
    val startPrice: TextFieldValue = TextFieldValue(),
    val priceStep: TextFieldValue = TextFieldValue(),
    val biasPrice: TextFieldValue = TextFieldValue(),
    val minSize: TextFieldValue = TextFieldValue(),
    val orderSize: TextFieldValue = TextFieldValue(),
    val sizeStep: TextFieldValue = TextFieldValue(),
    val orderAmount: TextFieldValue = TextFieldValue(),
    val priceForce: Boolean = false,
    val market: Boolean = false,
    val basePrec: TextFieldValue = TextFieldValue(),
    val quotePrec: TextFieldValue = TextFieldValue(),
    val active: Boolean = false,
) {
    companion object {
        val Init = ConfigEditViewState()
    }
}

@Immutable
sealed interface ConfigEditViewEvent {
    data object ConfigSaved : ConfigEditViewEvent
}