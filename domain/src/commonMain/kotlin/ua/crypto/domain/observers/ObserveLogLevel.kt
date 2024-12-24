package ua.crypto.domain.observers

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TiviPreferences
import ua.crypto.core.settings.TiviPreferences.LogLevel
import ua.crypto.domain.SubjectInteractor

@Inject
class ObserveLogLevel(
    private val preferences: TiviPreferences,
) : SubjectInteractor<Unit, LogLevel>() {

    override suspend fun createObservable(params: Unit): Flow<LogLevel> = preferences.loglevel.flow

    operator fun invoke() = invoke(Unit)
}