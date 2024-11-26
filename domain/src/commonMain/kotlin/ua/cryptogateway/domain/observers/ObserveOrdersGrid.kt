package ua.cryptogateway.domain.observers

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.models.db.OrdersGridEntity
import ua.cryptogateway.data.service.OrdersGridService
import ua.cryptogateway.domain.SuspendingWorkInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ObserveOrdersGrid(
    dispatchers: AppCoroutineDispatchers,
    private val service: OrdersGridService,
) : SuspendingWorkInteractor<Unit, List<OrdersGridEntity>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit): List<OrdersGridEntity> = withContext(dispatcher) {
        service.readAll()
    }
}