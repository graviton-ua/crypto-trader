package ua.hospes.cryptogateway.ui.configs.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.cryptogateway.domain.models.BotConfigModel
import ua.hospes.cryptogateway.ui.common.theme.AppTheme

@Composable
internal fun ConfigGroup(
    baseAsset: String,
    items: List<BotConfigModel>,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(text = baseAsset, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Medium)
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
                .clip(shape = MaterialTheme.shapes.medium),
        ) {
            ConfigTitleRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            HorizontalDivider()
            items.forEachIndexed { i, it ->
                ConfigItemRow(
                    state = it,
                    onEdit = onEdit,
                    modifier = Modifier.fillMaxWidth()
                        .background(color = if (i % 2 == 0) AppTheme.colors.even else AppTheme.colors.odd)
                        .padding(horizontal = 8.dp),
                )
            }
        }
    }
}

