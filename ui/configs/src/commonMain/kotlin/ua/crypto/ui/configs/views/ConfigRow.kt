package ua.crypto.ui.configs.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ConfigRow(
    baseAsset: @Composable RowScope.() -> Unit,
    quoteAsset: @Composable RowScope.() -> Unit,
    side: @Composable RowScope.() -> Unit,
    fond: @Composable RowScope.() -> Unit,
    startPrice: @Composable RowScope.() -> Unit,
    priceStep: @Composable RowScope.() -> Unit,
    biasPrice: @Composable RowScope.() -> Unit,
    minSize: @Composable RowScope.() -> Unit,
    orderSize: @Composable RowScope.() -> Unit,
    sizeStep: @Composable RowScope.() -> Unit,
    orderAmount: @Composable RowScope.() -> Unit,
    priceForce: @Composable RowScope.() -> Unit,
    market: @Composable RowScope.() -> Unit,
    basePrec: @Composable RowScope.() -> Unit,
    quotePrec: @Composable RowScope.() -> Unit,
    active: @Composable RowScope.() -> Unit,
    actions: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier,
    ) {
        baseAsset()
        quoteAsset()
        side()
        fond()
        startPrice()
        priceStep()
        biasPrice()
        minSize()
        orderSize()
        sizeStep()
        orderAmount()
        priceForce()
        market()
        basePrec()
        quotePrec()
        active()

        actions?.invoke(this)
    }
}


@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigRow(
            baseAsset = { Text(text = "BTC") },
            quoteAsset = { Text(text = "USDT") },
            side = { Text(text = "Ask") },
            fond = { Text(text = "fond") },
            startPrice = { Text(text = "10000") },
            priceStep = { Text(text = "priceStep") },
            biasPrice = { Text(text = "biasPrice") },
            minSize = { Text(text = "minSize") },
            orderSize = { Text(text = "orderSize") },
            sizeStep = { Text(text = "sizeStep") },
            orderAmount = { Text(text = "orderAmount") },
            priceForce = { Text(text = "priceForce") },
            market = { Text(text = "market") },
            basePrec = { Text(text = "basePrec") },
            quotePrec = { Text(text = "quotePrec") },
            active = { Text(text = "active") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}