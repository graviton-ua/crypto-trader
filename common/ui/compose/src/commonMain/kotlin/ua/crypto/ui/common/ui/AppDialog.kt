package ua.crypto.ui.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun AppDialog(
    title: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable ColumnScope.() -> Unit,
) = AppDialog(
    onClose = onClose,
    modifier = modifier,
    contentPadding = contentPadding,
    verticalArrangement = verticalArrangement,
) {
    ProvideTextStyle(value = MaterialTheme.typography.titleMedium) {
        title()
    }
    content()
}

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier.background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium),
    ) {
        onClose?.let {
            IconButton(
                onClick = it,
                modifier = Modifier.align(Alignment.TopEnd),
            ) { Icon(imageVector = Icons.Default.Close, contentDescription = null) }
        }

        Column(
            verticalArrangement = verticalArrangement,
            modifier = Modifier.padding(contentPadding),
        ) {
            /**
             * Provides a composition local value for [LocalUriHandler] that is guaranteed to be the same instance
             * throughout the composition hierarchy. This is important because [LocalUriHandler] is typically replaced
             * with a new instance each time the content is rendered, leading to potential issues. By using this provider,
             * you can ensure that the original [UriHandler] created by your application is used consistently.
             */
//            CompositionLocalProvider(
//                LocalUriHandler provides (LocalWhUriHandler.current ?: LocalUriHandler.current)
//            ) {
            content()
//            }
        }
    }
}