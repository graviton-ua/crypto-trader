package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.domain.models.BotConfigModel
import ua.cryptogateway.inject.injectViewModel
import ua.hospes.cryptogateway.ui.common.JsonNavType
import ua.hospes.cryptogateway.ui.common.navigation.ResultBackNavigator
import ua.hospes.cryptogateway.ui.common.theme.AppTheme
import ua.hospes.cryptogateway.ui.common.ui.AppCheckbox
import ua.hospes.cryptogateway.ui.configs.ConfigsComponent
import kotlin.reflect.typeOf

@Serializable
internal data class ConfigEditDialog(val config: BotConfigModel? = null) {
    companion object {
        val typeMap = mapOf(typeOf<BotConfigModel?>() to BotConfigModelArgType())
        fun from(savedStateHandle: SavedStateHandle) = savedStateHandle.toRoute<ConfigEditDialog>(typeMap)
    }
}

class BotConfigModelArgType(override val isNullableAllowed: Boolean = true) :
    JsonNavType<BotConfigModel?>(isNullableAllowed) {
    override fun fromJsonParse(value: String): BotConfigModel? = Json.decodeFromString(value)
    override fun BotConfigModel?.getJsonParse(): String = Json.encodeToString(this)
}

@Composable
internal fun ConfigEditDialog(
    diComponent: ConfigsComponent,
    savedStateHandle: SavedStateHandle,
    navigateUp: () -> Unit,
    resultNavigator: ResultBackNavigator<Boolean>,
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
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is ConfigEditViewEvent.ConfigSaved -> resultNavigator.navigateBack(true)
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    ConfigEditDialog(
        state = state,
        navigateUp = navigateUp,
        onBaseAssetChange = viewModel::onBaseAssetChange,
        onQuoteAssetChange = viewModel::onQuoteAssetChange,
        onSideSelect = viewModel::onSideSelect,
        onFondChange = viewModel::onFondChange,
        onStartPriceChange = viewModel::onStartPriceChange,
        onPriceStepChange = viewModel::onPriceStepChange,
        onBiasPriceChange = viewModel::onBiasPriceChange,
        onMinSizeChange = viewModel::onMinSizeChange,
        onOrderSizeChange = viewModel::onOrderSizeChange,
        onSizeStepChange = viewModel::onSizeStepChange,
        onOrderAmountChange = viewModel::onOrderAmountChange,
        onPriceForceChange = viewModel::onPriceForceChange,
        onMarketChange = viewModel::onMarketChange,
        onBasePrecChange = viewModel::onBasePrecChange,
        onQuotePrecChange = viewModel::onQuotePrecChange,
        onActiveChange = viewModel::onActiveChange,
        onDelete = viewModel::onDelete,
        onSave = viewModel::onSave,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfigEditDialog(
    state: ConfigEditViewState,
    navigateUp: () -> Unit,
    onBaseAssetChange: (TextFieldValue) -> Unit,
    onQuoteAssetChange: (TextFieldValue) -> Unit,
    onSideSelect: (Order.Side) -> Unit,
    onFondChange: (TextFieldValue) -> Unit,
    onStartPriceChange: (TextFieldValue) -> Unit,
    onPriceStepChange: (TextFieldValue) -> Unit,
    onBiasPriceChange: (TextFieldValue) -> Unit,
    onMinSizeChange: (TextFieldValue) -> Unit,
    onOrderSizeChange: (TextFieldValue) -> Unit,
    onSizeStepChange: (TextFieldValue) -> Unit,
    onOrderAmountChange: (TextFieldValue) -> Unit,
    onPriceForceChange: (Boolean) -> Unit,
    onMarketChange: (Boolean) -> Unit,
    onBasePrecChange: (TextFieldValue) -> Unit,
    onQuotePrecChange: (TextFieldValue) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onSave: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .width(IntrinsicSize.Max)
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
                .widthIn(min = 300.dp, max = 600.dp)
                .heightIn(min = 240.dp, max = 640.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = state.baseAsset,
                    onValueChange = onBaseAssetChange,
                    label = { Text(text = "Base asset") },
                    placeholder = { Text(text = "BTC") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.quoteAsset,
                    onValueChange = onQuoteAssetChange,
                    label = { Text(text = "Quote asset") },
                    placeholder = { Text(text = "USDT") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OrderSideDropdown(
                    selected = state.side,
                    onSelectItem = onSideSelect,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = state.fond,
                    onValueChange = onFondChange,
                    label = { Text(text = "Fond") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.startPrice,
                    onValueChange = onStartPriceChange,
                    label = { Text(text = "Start price") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.priceStep,
                    onValueChange = onPriceStepChange,
                    label = { Text(text = "Price step") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.biasPrice,
                    onValueChange = onBiasPriceChange,
                    label = { Text(text = "Bias price") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OutlinedTextField(
                    value = state.minSize,
                    onValueChange = onMinSizeChange,
                    label = { Text(text = "Min size") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.orderSize,
                    onValueChange = onOrderSizeChange,
                    label = { Text(text = "Order size") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.sizeStep,
                    onValueChange = onSizeStepChange,
                    label = { Text(text = "Size step") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.orderAmount,
                    onValueChange = onOrderAmountChange,
                    label = { Text(text = "Order amount") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AppCheckbox(
                    checked = state.priceForce,
                    onCheckedChange = onPriceForceChange,
                ) { Text(text = "Price force") }
                AppCheckbox(
                    checked = state.market,
                    onCheckedChange = onMarketChange,
                ) { Text(text = "Market") }
                OutlinedTextField(
                    value = state.basePrec,
                    onValueChange = onBasePrecChange,
                    label = { Text(text = "Base prec") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                OutlinedTextField(
                    value = state.quotePrec,
                    onValueChange = onQuotePrecChange,
                    label = { Text(text = "Quote prec") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
            }

            // Bottom panel with buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                ) { Text("Delete") }

                Spacer(modifier = Modifier.weight(1f))

                AppCheckbox(
                    checked = state.active,
                    onCheckedChange = onActiveChange,
                ) { Text(text = "Active") }

                OutlinedButton(
                    onClick = navigateUp,
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                ) { Text("Cancel") }
                Button(
                    onClick = onSave,
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                ) { Text("Save") }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    AppTheme {
        ConfigEditDialog(
            state = ConfigEditViewState.Init,
            navigateUp = {},
            onBaseAssetChange = {},
            onQuoteAssetChange = {},
            onSideSelect = {},
            onFondChange = {},
            onStartPriceChange = {},
            onPriceStepChange = {},
            onBiasPriceChange = {},
            onMinSizeChange = {},
            onOrderSizeChange = {},
            onSizeStepChange = {},
            onOrderAmountChange = {},
            onPriceForceChange = {},
            onMarketChange = {},
            onBasePrecChange = {},
            onQuotePrecChange = {},
            onActiveChange = {},
            onDelete = {},
            onSave = {},
        )
    }
}