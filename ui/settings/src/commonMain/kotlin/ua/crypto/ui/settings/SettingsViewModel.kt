package ua.crypto.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TraderPreferences.LogLevel
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.interactors.GetDbPort
import ua.crypto.domain.interactors.SetDbPort
import ua.crypto.domain.interactors.SetLogLevel
import ua.crypto.domain.observers.ObserveLogLevel

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
}