package ua.crypto.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.crypto.core.inject.injectViewModel
import ua.crypto.core.settings.TraderPreferences.LogLevel
import ua.crypto.ui.common.screens.RailScreen
import ua.crypto.ui.resources.Res
import ua.crypto.ui.resources.rail_screen_settings

@Serializable
data object SettingsScreen : RailScreen {
    override val icon: ImageVector = Icons.Default.Settings
    override val title: StringResource = Res.string.rail_screen_settings
}

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
        onPortChange = viewModel::onPortChange,
        onLogLevelSelect = viewModel::onLogLevelSelect,
        onFileLogLevelSelect = viewModel::onFileLogLevelSelect,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsViewState,
    onPortChange: (String) -> Unit,
    onLogLevelSelect: (LogLevel) -> Unit,
    onFileLogLevelSelect: (LogLevel) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(SettingsScreen.title)) },
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
                    .verticalScroll(state = rememberScrollState())
                    .padding(paddings)
                    .padding(24.dp),
            ) {
                OutlinedCard {
                    // Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = "Database settings",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    HorizontalDivider()


                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp),
                    ) {
                        OutlinedTextField(
                            value = state.port,
                            onValueChange = onPortChange,
                            label = { Text("Database connection port") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                OutlinedCard {
                    // Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = "Logs settings",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    HorizontalDivider()

                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp),
                    ) {
                        LogLevelDropdown(
                            title = "Console LogLevel",
                            selected = state.logLevel,
                            onSelectItem = onLogLevelSelect,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        LogLevelDropdown(
                            title = "File LogLevel",
                            selected = state.fileLogLevel,
                            onSelectItem = onFileLogLevelSelect,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LogLevelDropdown(
    title: String,
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
            label = { Text(title) },
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
            onPortChange = {},
            onLogLevelSelect = {},
            onFileLogLevelSelect = {},
        )
    }
}