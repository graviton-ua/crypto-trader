package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TiviPreferences
import ua.crypto.core.settings.TiviPreferences.LogLevel
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.ResultInteractor

@Inject
class SetLogLevel(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TiviPreferences,
) : ResultInteractor<LogLevel, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: LogLevel) = withContext(dispatcher) {
        preferences.loglevel.set(params)
    }
}