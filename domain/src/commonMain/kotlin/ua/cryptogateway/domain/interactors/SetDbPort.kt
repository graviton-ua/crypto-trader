package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.settings.TiviPreferences
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class SetDbPort(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TiviPreferences,
) : ResultInteractor<String, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: String) = withContext(dispatcher) {
        preferences.dbPort.set(params)
    }
}