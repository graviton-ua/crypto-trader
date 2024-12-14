package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import saschpe.log4k.logged
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.db.models.OrderEntity
import ua.cryptogateway.data.db.models.OrderType
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaActiveOrder
import ua.cryptogateway.domain.DataPuller
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.model.Side
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@ApplicationScope
@Inject
class ActiveOrdersPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val dao: OrderDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaActiveOrder>?>(null)

    init {
        //start()
        scope.updateActiveTable()
    }

    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "DataPuller job started" }
            DataPuller().pull(delay.value) { api.getActiveOrders() }
                .mapNotNull { it.getOrNull() }
                .catch { Log.error(tag = TAG, throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.value = it
                    it.logged()
                }
        }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "DataPuller job completed (exception: ${it?.message})" }; job = null } }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateActiveTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateActiveTable() job started" }

        data.filterNotNull()
            .map { it.map(KunaActiveOrder::toEntity) }
            .collectLatest { list ->
                Result.runCatching { dao.saveActive(list) }
//                    .onSuccess { Log.info(tag = TAG) { "ActiveTable updated" } }
                    .onFailure { Log.error(tag = TAG, throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateActiveTable() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "ActivePullService"
    }
}

private fun KunaActiveOrder.toEntity(): OrderEntity = OrderEntity(
    id, OrderType.fromKunaString(type), quantity, executedQuantity,
    cumulativeQuoteQty, cost, Side.fromKunaString(side), pair, price, status, createdAt, updatedAt
)