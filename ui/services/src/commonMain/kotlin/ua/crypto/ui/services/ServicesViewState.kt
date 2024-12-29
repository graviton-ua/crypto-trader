package ua.crypto.ui.services

import androidx.compose.runtime.Immutable
import ua.crypto.domain.services.Service

@Immutable
data class ServicesViewState(
    val services: List<AppService> = emptyList(),
) {

    @Immutable
    data class AppService(
        val name: String,
        val isRunning: Boolean,
        val isAuto: Boolean,
        val service: Service,
    )


    companion object {
        val Init = ServicesViewState()
    }
}