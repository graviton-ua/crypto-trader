package ua.hospes.cryptogateway.ui.configs.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.domain.models.BotConfigModel
import ua.hospes.cryptogateway.ui.common.formatDouble

@Composable
internal fun ConfigItemRow(
    state: BotConfigModel,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onClickEdit = remember(state, onEdit) { { onEdit(state.id) } }
    ConfigRow(
        baseAsset = { Text(text = state.baseAsset, modifier = Modifier.width(60.dp)) },
        quoteAsset = { Text(text = state.quoteAsset, modifier = Modifier.width(60.dp)) },
        side = { Text(text = state.side.name, modifier = Modifier.width(60.dp)) },
        fond = { Text(text = state.fond.formatDouble(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        startPrice = { Text(text = state.startPrice.formatDouble(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        priceStep = { Text(text = state.priceStep.formatDouble(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        biasPrice = { Text(text = state.biasPrice.formatDouble(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        minSize = { Text(text = state.minSize.formatDouble(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        orderSize = { Text(text = state.orderSize.toString(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        sizeStep = { Text(text = state.sizeStep.formatDouble(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        orderAmount = { Text(text = state.orderAmount.toString(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        priceForce = { CheckCrossBox(value = state.priceForce, modifier = Modifier.width(60.dp)) },
        market = { CheckCrossBox(value = state.market, modifier = Modifier.width(60.dp)) },
        basePrec = { Text(text = state.basePrec.toString(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        quotePrec = { Text(text = state.quotePrec.toString(), textAlign = TextAlign.End, modifier = Modifier.width(60.dp)) },
        active = { CheckCrossBox(value = state.active, modifier = Modifier.width(60.dp)) },
        actions = {
            IconButton(
                onClick = onClickEdit,
                //colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
            }
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    )
}

@Composable
private fun CheckCrossBox(
    value: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = if (value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val icon = if (value) Icons.Default.Check else Icons.Default.Close
    Box(contentAlignment = Alignment.Center, modifier = modifier) { Icon(imageVector = icon, contentDescription = null, tint = color) }
}


@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigItemRow(
            state = BotConfigModel(
                id = 0,
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
            onEdit = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}