package ua.cryptogateway.domain.observers

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.dao.OrdersGridDao
import ua.cryptogateway.data.db.models.OrdersGridEntity
import ua.cryptogateway.domain.SuspendingWorkInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class ObserveOrdersGrid(
    dispatchers: AppCoroutineDispatchers,
    private val service: OrdersGridDao,
) : SuspendingWorkInteractor<Unit, List<OrdersGridEntity>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Unit): List<OrdersGridEntity> = withContext(dispatcher) {
        service.readAll()
    }
}