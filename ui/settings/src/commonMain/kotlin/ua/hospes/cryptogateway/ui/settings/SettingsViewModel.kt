package ua.hospes.cryptogateway.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.interactors.GetDbPort
import ua.cryptogateway.domain.interactors.SetDbPort
import ua.cryptogateway.domain.interactors.SetLogLevel
import ua.cryptogateway.domain.observers.ObserveLogLevel
import ua.cryptogateway.settings.TiviPreferences.LogLevel
import ua.cryptogateway.util.AppCoroutineDispatchers

@OptIn(FlowPreview::class)
@Inject
class SettingsViewModel(
    dispatchers: AppCoroutineDispatchers,
    getDbPort: GetDbPort,
    observeLogLevel: ObserveLogLevel,
    setDbPort: SetDbPort,
    private val setLogLevel: SetLogLevel,
) : ViewModel() {

    private val port = MutableStateFlow("")
    private val logLevel = observeLogLevel.flow.onStart { emit(LogLevel.INFO) }

    val state: StateFlow<SettingsViewState> = combine(port, logLevel) { port, logLevel ->
        SettingsViewState(port, logLevel)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsViewState.Init,
    )

    init {
        observeLogLevel()

        viewModelScope.launch(dispatchers.io) {
            port.value = getDbPort.executeSync(Unit)
        }

        viewModelScope.launch(dispatchers.io) {
            port.debounce(500).collectLatest {
                setDbPort.executeSync(it)
            }
        }
    }

    fun onUpdatePort(text: String) = with(port) { value = text }

    fun onLogLevelSelect(level: LogLevel) {
        viewModelScope.launch { setLogLevel.executeSync(level) }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
    }
}