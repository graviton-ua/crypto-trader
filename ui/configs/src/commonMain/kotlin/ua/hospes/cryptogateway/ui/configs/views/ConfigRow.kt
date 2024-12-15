package ua.hospes.cryptogateway.ui.configs.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ConfigRow(
    baseAsset: @Composable () -> Unit,
    quoteAsset: @Composable () -> Unit,
    side: @Composable () -> Unit,
    fond: @Composable () -> Unit,
    startPrice: @Composable () -> Unit,
    priceStep: @Composable () -> Unit,
    biasPrice: @Composable () -> Unit,
    minSize: @Composable () -> Unit,
    orderSize: @Composable () -> Unit,
    sizeStep: @Composable () -> Unit,
    orderAmount: @Composable () -> Unit,
    priceForce: @Composable () -> Unit,
    market: @Composable () -> Unit,
    basePrec: @Composable () -> Unit,
    quotePrec: @Composable () -> Unit,
    active: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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