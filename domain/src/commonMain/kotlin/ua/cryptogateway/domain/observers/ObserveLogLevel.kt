package ua.cryptogateway.domain.observers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.SubjectInteractor
import ua.cryptogateway.domain.models.LogLevel
import ua.cryptogateway.settings.TiviPreferences
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ObserveLogLevel(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TiviPreferences,
) : SubjectInteractor<Unit, LogLevel?>() {
    private val dispatcher = dispatchers.io

    override suspend fun createObservable(params: Unit): Flow<LogLevel?> = preferences.loglevel.flow.map {
        when (it) {
            0 -> LogLevel.DEBUG
            1 -> LogLevel.INFO
            2 -> LogLevel.WARNING
            3 -> LogLevel.ERROR
            else -> null
        }
    }.flowOn(dispatcher)
}