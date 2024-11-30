package ua.cryptogateway.domain.observers

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.web.models.KunaMe
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.domain.SuspendingWorkInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ObserveMe(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
) : SuspendingWorkInteractor<Unit, Result<KunaMe>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit): Result<KunaMe> = withContext(dispatcher) {
        api.getMe()
    }
}