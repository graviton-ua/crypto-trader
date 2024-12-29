package ua.crypto.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TraderPreferences
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.services.TraderServiceInitializer

@OptIn(FlowPreview::class)
@Inject
class ServicesViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val prefs: TraderPreferences,
    private val services: Lazy<Set<TraderServiceInitializer>>,
) : ViewModel() {

    private val serviceStates = combine(services.value.map { service ->
        val name = service::class.simpleName!!
        combine(
            prefs.disabledServices.flow.map { !it.contains(name) },
            service.isRunning
        ) { disabled, isRunning ->
            ServicesViewState.AppService(
                name = name,
                isRunning = isRunning,
                isAuto = disabled,
                service = service,
            )
        }
    }) { it.toList() }.flowOn(dispatchers.io)

    val state: StateFlow<ServicesViewState> = combine(
        flowOf(12), serviceStates
    ) { _, services ->
        ServicesViewState(
            services = services,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ServicesViewState.Init,
    )


    fun onStartService(service: ServicesViewState.AppService) = with(service) { this.service.start() }
    fun onStopService(service: ServicesViewState.AppService) = with(service) { this.service.stop() }

    fun onAutoStartService(service: ServicesViewState.AppService) {
        viewModelScope.launch { prefs.disabledServices.update { list -> list.filterNot { it == service.name } } }
    }

    fun onAutoStopService(service: ServicesViewState.AppService) {
        viewModelScope.launch { prefs.disabledServices.update { list -> list + listOf(service.name) } }
    }
}