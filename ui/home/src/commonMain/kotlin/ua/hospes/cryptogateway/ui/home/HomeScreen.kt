package ua.hospes.cryptogateway.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    HomeScreen(
        onClick = viewModel::onClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddings ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .padding(paddings),
        ) {
            Button(onClick = onClick) {
                Text("Click me!")
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        HomeScreen(
            onClick = {},
        )
    }
}