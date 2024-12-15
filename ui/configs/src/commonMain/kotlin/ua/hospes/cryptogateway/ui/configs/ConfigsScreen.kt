package ua.hospes.cryptogateway.ui.configs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.domain.models.BotConfigModel
import ua.cryptogateway.inject.injectViewModel
import ua.hospes.cryptogateway.ui.common.navigation.OpenResultRecipient
import ua.hospes.cryptogateway.ui.configs.views.ConfigGroup

@Serializable
data object ConfigsScreen

@Composable
internal fun ConfigsScreen(
    diComponent: ConfigsComponent,
    navigateConfigEdit: (BotConfigModel?) -> Unit,
    resultConfigEdit: OpenResultRecipient<Unit>,
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
    navigateConfigEdit: (BotConfigModel?) -> Unit,
    resultConfigEdit: OpenResultRecipient<Unit>,
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
    onConfigEdit: (BotConfigModel?) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crypto Bot configs") },
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