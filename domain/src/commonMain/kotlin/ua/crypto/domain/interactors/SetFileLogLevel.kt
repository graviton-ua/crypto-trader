package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TraderPreferences
import ua.crypto.core.settings.TraderPreferences.LogLevel
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.ResultInteractor

@Inject
class SetFileLogLevel(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TraderPreferences,
) : ResultInteractor<LogLevel, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: LogLevel) = withContext(dispatcher) {
        preferences.fileLoglevel.set(params)
    }
}