package ua.crypto.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.crypto.core.inject.injectViewModel
import ua.crypto.ui.common.screens.RailScreen

@Serializable
data object HomeScreen : RailScreen {
    override val icon: ImageVector = Icons.Default.Home
}

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