package ua.crypto.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.crypto.core.inject.injectViewModel
import ua.crypto.core.settings.TiviPreferences.LogLevel

@Serializable
data object SettingsScreen

@Composable
internal fun SettingsScreen(
    diComponent: SettingsComponent,
) {
    SettingsScreen(
        viewModel = injectViewModel { diComponent.settingsViewModel() },
    )
}

@Composable
private fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsScreen(
        state = state,
        onPortUpdate = viewModel::onUpdatePort,
        onLogLevelSelect = viewModel::onLogLevelSelect,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsViewState,
    onPortUpdate: (String) -> Unit,
    onLogLevelSelect: (LogLevel) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddings ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .widthIn(min = 320.dp, max = 720.dp)
                    .padding(paddings)
                    .padding(24.dp),
            ) {
                OutlinedTextField(
                    value = state.port,
                    onValueChange = onPortUpdate,
                    label = { Text("Database connection port") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                LogLevelDropdown(
                    selected = state.logLevel,
                    onSelectItem = onLogLevelSelect,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LogLevelDropdown(
    selected: LogLevel,
    onSelectItem: (LogLevel) -> Unit,
    modifier: Modifier = Modifier,
    items: List<LogLevel> = LogLevel.entries,
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
            label = { Text("Log level") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
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


@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        SettingsScreen(
            state = SettingsViewState.Init,
            onPortUpdate = {},
            onLogLevelSelect = {},
        )
    }
}