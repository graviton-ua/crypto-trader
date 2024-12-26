package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.settings.TraderPreferences
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.domain.ResultInteractor

@Inject
class GetDbPort(
    dispatchers: AppCoroutineDispatchers,
    private val preferences: TraderPreferences,
) : ResultInteractor<Unit, String>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit): String = withContext(dispatcher) {
        preferences.dbPort.get()
    }
}