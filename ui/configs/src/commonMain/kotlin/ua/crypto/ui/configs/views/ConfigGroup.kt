package ua.crypto.ui.configs.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.crypto.domain.models.BotConfigModel
import ua.crypto.ui.common.theme.AppTheme
import ua.crypto.ui.common.ui.AppCard

@Composable
internal fun ConfigGroup(
    baseAsset: String,
    items: List<BotConfigModel>,
    onEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(
        title = {
            Text(
                text = baseAsset,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
            )
        },
        contentPadding = PaddingValues(),
        contentVerticalArrangement = Arrangement.Top,
        modifier = modifier.width(IntrinsicSize.Max),
    ) {
        ConfigTitleRow(
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalDivider()
        items.forEachIndexed { i, it ->
            ConfigItemRow(
                state = it,
                onEdit = onEdit,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (i % 2 == 0) AppTheme.colors.even else AppTheme.colors.odd),
            )
        }
    }
}

