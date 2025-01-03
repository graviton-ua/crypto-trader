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
import ua.crypto.domain.interactors.SetFileLogLevel
import ua.crypto.domain.interactors.SetLogLevel
import ua.crypto.domain.observers.ObserveFileLogLevel
import ua.crypto.domain.observers.ObserveLogLevel

@OptIn(FlowPreview::class)
@Inject
class SettingsViewModel(
    dispatchers: AppCoroutineDispatchers,

    getDbPort: GetDbPort,
    observeLogLevel: ObserveLogLevel,
    observeFileLogLevel: ObserveFileLogLevel,

    setDbPort: SetDbPort,
    private val setLogLevel: SetLogLevel,
    private val setFileLogLevel: SetFileLogLevel,
) : ViewModel() {

    private val port = MutableStateFlow("")

    val state: StateFlow<SettingsViewState> = combine(
        port, observeLogLevel.flow, observeFileLogLevel.flow
    ) { port, logLevel, fileLogLevel ->
        SettingsViewState(
            port = port,
            logLevel = logLevel,
            fileLogLevel = fileLogLevel,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsViewState.Init,
    )

    init {
        observeLogLevel()
        observeFileLogLevel()

        viewModelScope.launch(dispatchers.io) {
            port.value = getDbPort.executeSync(Unit)
        }

        viewModelScope.launch(dispatchers.io) {
            port.debounce(500).collectLatest {
                setDbPort.executeSync(it)
            }
        }
    }

    fun onPortChange(text: String) = with(port) { value = text }

    fun onLogLevelSelect(level: LogLevel) {
        viewModelScope.launch { setLogLevel.executeSync(level) }
    }

    fun onFileLogLevelSelect(level: LogLevel) {
        viewModelScope.launch { setFileLogLevel.executeSync(level) }
    }
}