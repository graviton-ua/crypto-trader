package ua.crypto.ui.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.crypto.core.inject.injectViewModel
import ua.crypto.ui.common.screens.RailScreen
import ua.crypto.ui.common.theme.AppTheme
import ua.crypto.ui.common.ui.AppCard
import ua.crypto.ui.common.ui.AppSwitch
import ua.crypto.ui.resources.Res
import ua.crypto.ui.resources.rail_screen_services

@Serializable
data object ServicesScreen : RailScreen {
    override val icon: ImageVector = Icons.Default.Tune
    override val title: StringResource = Res.string.rail_screen_services
}

@Composable
internal fun ServicesScreen(
    diComponent: ServicesComponent,
) {
    ServicesScreen(
        viewModel = injectViewModel { diComponent.servicesViewModel() },
    )
}

@Composable
private fun ServicesScreen(
    viewModel: ServicesViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ServicesScreen(
        state = state,
        onStartService = viewModel::onStartService,
        onStopService = viewModel::onStopService,
        onEnableService = viewModel::onAutoStartService,
        onDisableService = viewModel::onAutoStopService,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ServicesScreen(
    state: ServicesViewState,
    onStartService: (ServicesViewState.AppService) -> Unit,
    onStopService: (ServicesViewState.AppService) -> Unit,
    onEnableService: (ServicesViewState.AppService) -> Unit,
    onDisableService: (ServicesViewState.AppService) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(ServicesScreen.title)) },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { paddings ->
        FlowRow(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(state = rememberScrollState())
                .padding(paddings)
                .padding(24.dp),
        ) {
            state.services.forEach { service ->
                ServiceCard(
                    service = service,
                    onStart = remember(onStartService, service) { { onStartService(service) } },
                    onStop = remember(onStopService, service) { { onStopService(service) } },
                    onEnable = remember(onEnableService, service) { { onEnableService(service) } },
                    onDisable = remember(onDisableService, service) { { onDisableService(service) } },
                    modifier = Modifier.widthIn(min = 300.dp, max = 420.dp),
                )
            }
        }
    }
}


@Composable
private fun ServiceCard(
    service: ServicesViewState.AppService,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onEnable: () -> Unit,
    onDisable: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(
        title = {
            Text(text = service.name)
            Spacer(Modifier.weight(1f))
            Text(
                text = if (service.isRunning) "ON" else "OFF",
                textAlign = TextAlign.Center,
                color = if (service.isRunning) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .defaultMinSize(minWidth = 70.dp)
                    .clip(shape = CircleShape)
                    .background(color = if (service.isRunning) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background)
                    .padding(vertical = 2.dp, horizontal = 6.dp),
            )
        },
        modifier = modifier,
    ) {
        AppSwitch(
            checked = service.isRunning,
            onCheckedChange = remember(onStart, onStart) { { if (it) onStart() else onStop() } },
            modifier = Modifier.fillMaxWidth(),
        ) { Text(text = "Enabled") }

        AppSwitch(
            checked = service.isAuto,
            onCheckedChange = remember(onEnable, onDisable) { { if (it) onEnable() else onDisable() } },
            modifier = Modifier.fillMaxWidth(),
        ) { Text(text = "Auto start enabled") }
    }
}


@Preview
@Composable
private fun Preview() {
    AppTheme {
        ServicesScreen(
            state = ServicesViewState.Init,
            onStartService = {},
            onStopService = {},
            onEnableService = {},
            onDisableService = {},
        )
    }
}