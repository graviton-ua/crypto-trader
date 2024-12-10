package ua.cryptogateway.domain.observers

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.SubjectInteractor
import ua.cryptogateway.settings.TiviPreferences
import ua.cryptogateway.settings.TiviPreferences.LogLevel

@Inject
class ObserveLogLevel(
    private val preferences: TiviPreferences,
) : SubjectInteractor<Unit, LogLevel>() {

    override suspend fun createObservable(params: Unit): Flow<LogLevel> = preferences.loglevel.flow

    operator fun invoke() = invoke(Unit)
}