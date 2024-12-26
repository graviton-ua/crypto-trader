package ua.crypto.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TraderPreferences.LogLevel
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.interactors.*
import ua.crypto.domain.observers.ObserveLogLevel

@OptIn(FlowPreview::class)
@Inject
class SettingsViewModel(
    dispatchers: AppCoroutineDispatchers,

    getDbPort: GetDbPort,
    observeLogLevel: ObserveLogLevel,
    getKunaApiKey: GetKunaApiKey,

    setDbPort: SetDbPort,
    private val setLogLevel: SetLogLevel,
    setKunaApiKey: SetKunaApiKey,
) : ViewModel() {

    private val port = MutableStateFlow("")
    private val logLevel = observeLogLevel.flow.onStart { emit(LogLevel.INFO) }
    private val kunaApiKey = MutableStateFlow("")

    val state: StateFlow<SettingsViewState> = combine(
        port, logLevel, kunaApiKey
    ) { port, logLevel, kunaApiKey ->
        SettingsViewState(
            port = port,
            logLevel = logLevel,
            kunaApiKey = kunaApiKey,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsViewState.Init,
    )

    init {
        observeLogLevel()

        viewModelScope.launch(dispatchers.io) {
            port.value = getDbPort.executeSync(Unit)
            kunaApiKey.value = getKunaApiKey.executeSync(Unit)
        }

        viewModelScope.launch(dispatchers.io) {
            port.debounce(500).collectLatest {
                setDbPort.executeSync(it)
            }
        }

        viewModelScope.launch(dispatchers.io) {
            kunaApiKey.debounce(500).collectLatest {
                setKunaApiKey.executeSync(it)
            }
        }
    }

    fun onPortChange(text: String) = with(port) { value = text }

    fun onLogLevelSelect(level: LogLevel) {
        viewModelScope.launch { setLogLevel.executeSync(level) }
    }

    fun onKunaApiKeyChange(text: String) = with(kunaApiKey) { value = text }
}