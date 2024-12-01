package ua.hospes.cryptogateway.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.settings.TiviPreferences
import ua.cryptogateway.util.AppCoroutineDispatchers

@OptIn(FlowPreview::class)
@Inject
class SettingsViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val tiviPreferences: TiviPreferences,
) : ViewModel() {

    private val _port = MutableStateFlow("")
    val port: StateFlow<String> = _port

    init {
        viewModelScope.launch(dispatchers.io) {
            _port.value = tiviPreferences.dbPort.get()
        }

        viewModelScope.launch(dispatchers.io) {
            tiviPreferences.dbPort.flow.collectLatest {
                Log.info(tag = TAG) { "DB port: $it" }
            }
        }

        viewModelScope.launch(dispatchers.io) {
            _port.debounce(500).collectLatest {
                tiviPreferences.dbPort.set(it)
            }
        }
    }

    fun onUpdatePort(text: String) = with(_port) { value = text }


    companion object {
        private const val TAG = "SettingsViewModel"
    }
}