package ua.crypto.ui.configs.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ConfigTitleRow(
    modifier: Modifier = Modifier,
) {
    ConfigRow(
        baseAsset = {
            Text(
                text = "Base\nasset", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        quoteAsset = {
            Text(
                text = "Quote\nasset", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        side = {
            Text(
                text = "Side", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        fond = {
            Text(
                text = "Fond", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        startPrice = {
            Text(
                text = "Start\nprice", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        priceStep = {
            Text(
                text = "Price\nstep", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        biasPrice = {
            Text(
                text = "Bias\nprice", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        minSize = {
            Text(
                text = "Min\nsize", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        orderSize = {
            Text(
                text = "Order\nsize", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        sizeStep = {
            Text(
                text = "Size\nstep", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        orderAmount = {
            Text(
                text = "Order\namount", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        priceForce = {
            Text(
                text = "Price\nforce", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        market = {
            Text(
                text = "Market", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        basePrec = {
            Text(
                text = "Base\nprec", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        quotePrec = {
            Text(
                text = "Quote\nprec", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        active = {
            Text(
                text = "Active", textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.width(60.dp),
            )
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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