package ua.hospes.cryptogateway.ui.configs.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ConfigTitleRow(
    modifier: Modifier = Modifier,
) {
    ConfigRow(
        baseAsset = { Text(text = "Base asset") },
        quoteAsset = { Text(text = "Quote asset") },
        side = { Text(text = "Side") },
        fond = { Text(text = "Fond") },
        startPrice = { Text(text = "Start price") },
        priceStep = { Text(text = "Price step") },
        biasPrice = { Text(text = "Bias price") },
        minSize = { Text(text = "Min size") },
        orderSize = { Text(text = "Order size") },
        sizeStep = { Text(text = "Size step") },
        orderAmount = { Text(text = "Order amount") },
        priceForce = { Text(text = "Price force") },
        market = { Text(text = "Market") },
        basePrec = { Text(text = "Base prec") },
        quotePrec = { Text(text = "Quote prec") },
        active = { Text(text = "Active") },
        modifier = modifier,
    )
}


@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigTitleRow(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}