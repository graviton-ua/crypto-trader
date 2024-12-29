package ua.crypto.ui.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun AppDialog(
    title: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    titleVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    titleHorizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    contentVerticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(8.dp),
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable ColumnScope.() -> Unit,
) = AppDialog(
    modifier = Modifier
        .width(IntrinsicSize.Max)
        .then(modifier),
) {
    Row(
        verticalAlignment = titleVerticalAlignment,
        horizontalArrangement = titleHorizontalArrangement,
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
            title()
        }
    }
    HorizontalDivider()
    Column(
        verticalArrangement = contentVerticalArrangement,
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
    ) {
        content()
    }
}

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = verticalArrangement,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(contentPadding),
    ) {
        /**
         * Provides a composition local value for [LocalUriHandler] that is guaranteed to be the same instance
         * throughout the composition hierarchy. This is important because [LocalUriHandler] is typically replaced
         * with a new instance each time the content is rendered, leading to potential issues. By using this provider,
         * you can ensure that the original [UriHandler] created by your application is used consistently.
         */
//        CompositionLocalProvider(
//            LocalUriHandler provides (LocalWhUriHandler.current ?: LocalUriHandler.current)
//        ) {
        content()
//        }
    }
}