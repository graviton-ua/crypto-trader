package ua.cryptogateway.domain.observers

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.web.models.KunaFee
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.domain.SuspendingWorkInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ObserveFees(
    dispatchers: AppCoroutineDispatchers,
    private val api: KunaApi,
) : SuspendingWorkInteractor<Unit, Result<List<KunaFee>>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit): Result<List<KunaFee>> = withContext(dispatcher) {
        api.getFees()
    }
}