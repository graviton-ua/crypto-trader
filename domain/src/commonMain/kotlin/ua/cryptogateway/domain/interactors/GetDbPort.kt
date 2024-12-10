package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.settings.TiviPreferences
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class GetDbPort(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TiviPreferences,
) : ResultInteractor<Unit, String>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit): String = withContext(dispatcher) {
        preferences.dbPort.get()
    }
}