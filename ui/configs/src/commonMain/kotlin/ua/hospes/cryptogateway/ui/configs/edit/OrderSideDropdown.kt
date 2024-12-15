package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import ua.cryptogateway.data.models.Order

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OrderSideDropdown(
    selected: Order.Side,
    onSelectItem: (Order.Side) -> Unit,
    modifier: Modifier = Modifier,
    items: List<Order.Side> = Order.Side.entries,
) {
    val expanded = remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selected.name,
            onValueChange = {},
            label = { Text("Side") },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded.value) },
            modifier = Modifier
                .fillMaxWidth()
                .pointerHoverIcon(PointerIcon.Hand, true)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.exposedDropdownSize(true),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.name) },
                    onClick = {
                        onSelectItem(item)
                        expanded.value = false
                    },
                )
            }
        }
    }
}
