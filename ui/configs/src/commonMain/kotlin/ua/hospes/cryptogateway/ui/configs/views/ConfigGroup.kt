package ua.hospes.cryptogateway.ui.configs.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ua.cryptogateway.domain.models.BotConfigModel

@Composable
internal fun ConfigGroup(
    baseAsset: String,
    items: List<BotConfigModel>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = baseAsset)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            ConfigTitleRow(modifier = Modifier.fillMaxWidth())
            items.forEachIndexed { i, it ->
                ConfigItemRow(
                    state = it,
                    modifier = Modifier.fillMaxWidth()
                        .background(color = if (i % 2 != 0) Color.LightGray else Color.Transparent),
                )
            }
        }
    }
}