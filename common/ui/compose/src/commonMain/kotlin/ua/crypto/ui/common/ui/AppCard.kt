package ua.crypto.ui.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.outlinedShape,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    elevation: CardElevation = CardDefaults.outlinedCardElevation(),
    border: BorderStroke = CardDefaults.outlinedCardBorder(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    title: (@Composable RowScope.() -> Unit)? = null,
    titleVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    titleHorizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    contentVerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    OutlinedCard(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
    ) {
        title?.let {
            Row(
                verticalAlignment = titleVerticalAlignment,
                horizontalArrangement = titleHorizontalArrangement,
                modifier = Modifier.padding(contentPadding),
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
                    it.invoke(this)
                }
            }
            HorizontalDivider()
        }

        Column(
            verticalArrangement = contentVerticalArrangement,
            modifier = Modifier.padding(contentPadding),
            content = content,
        )
    }
}