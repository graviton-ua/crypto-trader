package ua.hospes.cryptogateway.ui.configs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.inject.injectViewModel
import ua.hospes.cryptogateway.ui.configs.views.ConfigGroup

@Serializable
data object ConfigsScreen

@Composable
internal fun ConfigsScreen(
    diComponent: ConfigsComponent,
) {
    ConfigsScreen(
        viewModel = injectViewModel { diComponent.configsViewModel() },
    )
}

@Composable
private fun ConfigsScreen(
    viewModel: ConfigsViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ConfigsScreen(
        state = state,
        onClick = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigsScreen(
    state: ConfigsViewState,
    onClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crypto Bot configs") },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddings ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(paddings)
                .verticalScroll(rememberScrollState())
                .horizontalScroll(rememberScrollState()),
        ) {
            state.groups.forEach {
                ConfigGroup(
                    baseAsset = it.key,
                    items = it.value,
                    modifier = Modifier.fillMaxWidth(),
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
            onClick = {},
        )
    }
}