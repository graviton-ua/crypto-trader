package ua.hospes.cryptogateway.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    SettingsScreen()
}

@Composable
private fun SettingsScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = "Setting screen")
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        SettingsScreen()
    }
}