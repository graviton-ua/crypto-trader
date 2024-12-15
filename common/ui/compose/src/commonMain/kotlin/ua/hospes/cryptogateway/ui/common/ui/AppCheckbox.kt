package ua.hospes.cryptogateway.ui.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import ua.hospes.cryptogateway.ui.common.theme.AppTheme

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(checkedColor = AppTheme.colors.primary),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) = Checkbox(checked, onCheckedChange, modifier.pointerHoverIcon(PointerIcon.Hand), enabled, colors, interactionSource)

@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    colors: CheckboxColors = CheckboxDefaults.colors(checkedColor = AppTheme.colors.primary),
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
        modifier = modifier
            .pointerHoverIcon(PointerIcon.Hand)
            .clip(shape = AppTheme.shapes.small)
            .let { m -> onCheckedChange?.let { m.clickable(enabled = enabled) { onCheckedChange(!checked) } } ?: m }
            .padding(contentPadding),
    ) {
        AppCheckbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = colors,
        )

        content()
    }
}


@Preview
@Composable
private fun Preview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) checked: Boolean,
) {
    AppTheme {
        AppCheckbox(
            checked = checked,
            onCheckedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewWithContent(
    @PreviewParameter(BooleanPreviewParameterProvider::class) checked: Boolean,
) {
    AppTheme {
        AppCheckbox(
            checked = checked,
            onCheckedChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text(text = "Example with content") }
    }
}