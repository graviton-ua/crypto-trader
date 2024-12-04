package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.domain.models.LogLevel
import ua.cryptogateway.settings.TiviPreferences
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class SetLogLevel(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TiviPreferences,
) : ResultInteractor<LogLevel, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: LogLevel) = withContext(dispatcher) {
        preferences.loglevel.set(params.ordinal)
    }
}