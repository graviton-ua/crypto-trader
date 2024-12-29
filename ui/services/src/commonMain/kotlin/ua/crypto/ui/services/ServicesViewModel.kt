package ua.crypto.ui.services

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
class ServicesViewModel(
    dispatchers: AppCoroutineDispatchers,

    getDbPort: GetDbPort,
    observeLogLevel: ObserveLogLevel,

    setDbPort: SetDbPort,
    private val setLogLevel: SetLogLevel,
) : ViewModel() {

    private val port = MutableStateFlow("")
    private val logLevel = observeLogLevel.flow.onStart { emit(LogLevel.INFO) }

    val state: StateFlow<ServicesViewState> = combine(
        port, logLevel
    ) { port, logLevel ->
        ServicesViewState(
            port = port,
            logLevel = logLevel,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ServicesViewState.Init,
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

    fun onPortChange(text: String) = with(port) { value = text }

    fun onLogLevelSelect(level: LogLevel) {
        viewModelScope.launch { setLogLevel.executeSync(level) }
    }
}