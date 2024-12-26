package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TraderPreferences
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.ResultInteractor

@Inject
class SetDbPort(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TraderPreferences,
) : ResultInteractor<String, Unit>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: String) = withContext(dispatcher) {
        preferences.dbPort.set(params)
    }
}