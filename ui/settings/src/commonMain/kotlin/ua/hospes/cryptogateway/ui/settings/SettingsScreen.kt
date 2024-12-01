package ua.hospes.cryptogateway.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.inject.injectViewModel

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
    val port by viewModel.port.collectAsStateWithLifecycle()
    SettingsScreen(
        port = port,
        onPortUpdate = viewModel::onUpdatePort,
    )
}

@Composable
private fun SettingsScreen(
    port: String,
    onPortUpdate: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = "Setting screen")
        OutlinedTextField(
            value = port,
            onValueChange = onPortUpdate,
            label = { Text("Database connection port") },
            singleLine = true,
            modifier = Modifier.defaultMinSize(minWidth = 240.dp),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        SettingsScreen(
            port = "1234",
            onPortUpdate = {},
        )
    }
}