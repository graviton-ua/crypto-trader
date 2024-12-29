package ua.crypto.ui.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.services.TraderServiceInitializer

@OptIn(FlowPreview::class)
@Inject
class ServicesViewModel(
    dispatchers: AppCoroutineDispatchers,
    private val services: Lazy<Set<TraderServiceInitializer>>,
) : ViewModel() {

    private val serviceStates = combine(services.value.map { service ->
        service.isRunning.map { isRunning ->
            ServicesViewState.AppService(
                name = service::class.simpleName!!,
                isRunning = isRunning,
                service = service,
            )
        }
    }) { it.toList() }

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
}