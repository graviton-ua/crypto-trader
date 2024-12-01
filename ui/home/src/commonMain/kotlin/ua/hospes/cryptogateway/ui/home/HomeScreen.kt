package ua.hospes.cryptogateway.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.inject.injectViewModel

@Serializable
data object HomeScreen

@Composable
internal fun HomeScreen(
    diComponent: HomeComponent,
) {
    HomeScreen(
        viewModel = injectViewModel { diComponent.homeViewModel() },
    )
}

@Composable
private fun HomeScreen(
    viewModel: HomeViewModel,
) {
    HomeScreen()
}

@Composable
private fun HomeScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Button(onClick = {}) {
            Text("Click me!")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        HomeScreen()
    }
}