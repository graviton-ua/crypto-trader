package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.domain.models.BotConfigModel
import ua.cryptogateway.inject.injectViewModel
import ua.hospes.cryptogateway.ui.common.JsonNavType
import ua.hospes.cryptogateway.ui.common.navigation.ResultBackNavigator
import ua.hospes.cryptogateway.ui.configs.ConfigsComponent
import kotlin.reflect.typeOf

@Serializable
internal data class ConfigEditDialog(val config: BotConfigModel? = null) {
    companion object {
        val typeMap = mapOf(typeOf<BotConfigModel?>() to BotConfigModelArgType())
        fun from(savedStateHandle: SavedStateHandle) = savedStateHandle.toRoute<ConfigEditDialog>(typeMap)
    }
}

class BotConfigModelArgType(override val isNullableAllowed: Boolean = true) : JsonNavType<BotConfigModel?>(isNullableAllowed) {
    override fun fromJsonParse(value: String): BotConfigModel? = Json.decodeFromString(value)
    override fun BotConfigModel?.getJsonParse(): String = Json.encodeToString(this)
}

@Composable
internal fun ConfigEditDialog(
    diComponent: ConfigsComponent,
    savedStateHandle: SavedStateHandle,
    navigateUp: () -> Unit,
    resultNavigator: ResultBackNavigator<Unit>,
) {
    ConfigEditDialog(
        viewModel = injectViewModel { diComponent.configEditViewModel() }
            .also { it.initConfig(ConfigEditDialog.from(savedStateHandle).config) },
        navigateUp = navigateUp,
        resultNavigator = resultNavigator,
    )
}

@Composable
private fun ConfigEditDialog(
    viewModel: ConfigEditViewModel,
    navigateUp: () -> Unit,
    resultNavigator: ResultBackNavigator<Unit>,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ConfigEditDialog(
        state = state,
        navigateUp = navigateUp,
        onBaseAssetChange = viewModel::onBaseAssetChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigEditDialog(
    state: ConfigEditViewState,
    navigateUp: () -> Unit,
    onBaseAssetChange: (TextFieldValue) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.medium),
    ) {
        Text(
            text = state.title?.let { "Edit \"$it\" config" } ?: "Create config",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 24.dp, start = 24.dp, end = 24.dp),
        )

        HorizontalDivider()

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
                .width(300.dp)
                .heightIn(min = 120.dp, max = 640.dp),
        ) {
            OutlinedTextField(
                value = state.baseAsset,
                onValueChange = onBaseAssetChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        ConfigEditDialog(
            state = ConfigEditViewState.Init,
            navigateUp = {},
            onBaseAssetChange = {},
        )
    }
}