package ua.hospes.cryptogateway.ui.configs.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.domain.models.BotConfigModel

@Composable
internal fun ConfigItemRow(
    state: BotConfigModel,
    modifier: Modifier = Modifier,
) {
    ConfigRow(
        baseAsset = { Text(text = state.baseAsset) },
        quoteAsset = { Text(text = state.quoteAsset) },
        side = { Text(text = state.side.name) },
        fond = { Text(text = state.fond.let { String.format("%.8f", it) }) },
        startPrice = { Text(text = state.startPrice.let { String.format("%.8f", it) }) },
        priceStep = { Text(text = state.priceStep.let { String.format("%.8f", it) }) },
        biasPrice = { Text(text = state.biasPrice.let { String.format("%.8f", it) }) },
        minSize = { Text(text = state.minSize.let { String.format("%.8f", it) }) },
        orderSize = { Text(text = state.orderSize.toString()) },
        sizeStep = { Text(text = state.sizeStep.let { String.format("%.8f", it) }) },
        orderAmount = { Text(text = state.orderAmount.toString()) },
        priceForce = { Text(text = state.priceForce.toString()) },
        market = { Text(text = state.market.toString()) },
        basePrec = { Text(text = state.basePrec.toString()) },
        quotePrec = { Text(text = state.quotePrec.toString()) },
        active = { Text(text = state.active.toString()) },
        modifier = modifier,
    )
}


@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigItemRow(
            state = BotConfigModel(
                baseAsset = "BTC",
                quoteAsset = "USDT",
                side = Order.Side.Sell,
                fond = 12.0,
                startPrice = 0.3,
                priceStep = 0.6,
                biasPrice = 1.2,
                minSize = 0.0005,
                orderSize = 1,
                sizeStep = 1.0,
                orderAmount = 5,
                priceForce = false,
                market = false,
                basePrec = 8,
                quotePrec = 8,
                active = true,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}