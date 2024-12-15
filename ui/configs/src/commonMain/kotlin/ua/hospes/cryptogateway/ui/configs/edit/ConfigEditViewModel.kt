package ua.hospes.cryptogateway.ui.configs.edit

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.domain.models.BotConfigModel
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ConfigEditViewModel(
    //@Assisted savedStateHandle: SavedStateHandle, TODO: Library not working properly with Assisted injection yet
    dispatchers: AppCoroutineDispatchers,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    private val config = MutableStateFlow<BotConfigModel?>(null)

    private val title = config.map { it?.let { "${it.baseAsset}_${it.quoteAsset}" } }
    private val baseAsset = MutableStateFlow(TextFieldValue())

    val state = combine(
        title, baseAsset,
    ) { title, base ->
        ConfigEditViewState(
            title = title,
            baseAsset = base,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ConfigEditViewState.Init,
    )


    init {
        viewModelScope.launch(dispatcher) {
            config.collect {
                Log.info(tag = TAG) { "Config to edit: $it" }
                it?.baseAsset?.let { baseAsset.value = TextFieldValue(text = it, selection = TextRange(it.length)) }
            }
        }
    }

    fun initConfig(config: BotConfigModel?) {
        Log.info(tag = TAG) { "initConfig: $config" }
        this.config.value = config
    }

    fun onBaseAssetChange(text: TextFieldValue) = with(baseAsset) { value = text }


    companion object {
        private const val TAG = "ConfigEditViewModel"
    }
}