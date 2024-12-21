package ua.hospes.cryptogateway.ui.configs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.observers.ObserveBotConfigs
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ConfigsViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val observeBotConfigs: ObserveBotConfigs,
) : ViewModel() {
    private val dispatcher = dispatchers.io

    private val botConfigs = observeBotConfigs.flow.map { it.groupBy { it.baseAsset } }

    val state = combine(
        botConfigs, flowOf(123),
    ) { configs, _ ->
        ConfigsViewState(
            groups = configs,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ConfigsViewState.Init,
    )


    init {
        observeBotConfigs()
    }


    fun refreshList() = viewModelScope.launch { observeBotConfigs.silentRefresh() }.let { }
}