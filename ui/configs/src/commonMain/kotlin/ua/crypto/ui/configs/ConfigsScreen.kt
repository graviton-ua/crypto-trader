package ua.crypto.ui.configs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.crypto.core.inject.injectViewModel
import ua.crypto.ui.common.navigation.OpenResultRecipient
import ua.crypto.ui.common.screens.RailScreen
import ua.crypto.ui.configs.views.ConfigGroup
import ua.crypto.ui.resources.Res
import ua.crypto.ui.resources.rail_screen_configs

@Serializable
data object ConfigsScreen : RailScreen {
    override val icon: ImageVector = Icons.Default.SmartToy
    override val title: StringResource = Res.string.rail_screen_configs
}

@Composable
internal fun ConfigsScreen(
    diComponent: ConfigsComponent,
    navigateConfigEdit: (Int?) -> Unit,
    resultConfigEdit: OpenResultRecipient<Boolean>,
) {
    ConfigsScreen(
        viewModel = injectViewModel { diComponent.configsViewModel() },
        navigateConfigEdit = navigateConfigEdit,
        resultConfigEdit = resultConfigEdit,
    )
}

@Composable
private fun ConfigsScreen(
    viewModel: ConfigsViewModel,
    navigateConfigEdit: (Int?) -> Unit,
    resultConfigEdit: OpenResultRecipient<Boolean>,
) {
    resultConfigEdit.onNavResult { viewModel.refreshList() }

    val state by viewModel.state.collectAsStateWithLifecycle()
    ConfigsScreen(
        state = state,
        onConfigEdit = navigateConfigEdit,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigsScreen(
    state: ConfigsViewState,
    onConfigEdit: (Int?) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(ConfigsScreen.title)) },
                actions = {
                    OutlinedButton(
                        onClick = { onConfigEdit(null) },
                        modifier = Modifier.padding(end = 8.dp)
                            .pointerHoverIcon(PointerIcon.Hand),
                    ) {
                        Text("Add new", modifier = Modifier.padding(end = 8.dp))
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Add")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddings ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(24.dp),
            modifier = Modifier.fillMaxSize()
                .padding(paddings),
        ) {
            items(items = state.groups.toList()) { (asset, items) ->
                ConfigGroup(
                    baseAsset = asset,
                    items = items,
                    onEdit = onConfigEdit,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigsScreen(
            state = ConfigsViewState.Init,
            onConfigEdit = {},
        )
    }
}