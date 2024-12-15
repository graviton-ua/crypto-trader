package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.inject.injectViewModel
import ua.hospes.cryptogateway.ui.common.navigation.ResultBackNavigator
import ua.hospes.cryptogateway.ui.configs.ConfigsComponent
import ua.hospes.cryptogateway.ui.configs.views.ConfigGroup

@Serializable
data object ConfigEditDialog

@Composable
internal fun ConfigEditDialog(
    diComponent: ConfigsComponent,
    resultNavigator: ResultBackNavigator<Unit>,
) {
    ConfigEditDialog(
        viewModel = injectViewModel { diComponent.configEditViewModel() },
        resultNavigator = resultNavigator,
    )
}

@Composable
private fun ConfigEditDialog(
    viewModel: ConfigEditViewModel,
    resultNavigator: ResultBackNavigator<Unit>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ConfigEditDialog(
        state = state,
        onClick = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigEditDialog(
    state: ConfigEditViewState,
    onClick: () -> Unit,
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
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
                .padding(paddings)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            state.groups.forEach {
                ConfigGroup(
                    baseAsset = it.key,
                    items = it.value,
                    onEdit = {},
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigEditDialog(
            state = ConfigEditViewState.Init,
            onClick = {},
        )
    }
}