package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import saschpe.log4k.logged
import ua.cryptogateway.data.db.dao.ActiveDao
import ua.cryptogateway.data.db.models.ActiveEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaActive
import ua.cryptogateway.domain.DataPuller
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers
import ua.cryptogateway.inject.ApplicationCoroutineScope
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@ApplicationScope
@Inject
class ActivePullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val dao: ActiveDao,
) {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaActive>?>(null)

    init {
        //start()
        scope.updateActiveTable()
    }

    fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "DataPuller job started" }
            DataPuller().pull(delay.value) { api.getActive() }
                .mapNotNull { it.getOrNull() }
                .catch { Log.error(tag = TAG, throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.value = it
                    it.logged()
                }
        }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "DataPuller job completed (exception: ${it?.message})" }; job = null } }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    fun restart() {
        stop()
        start()
    }

    private fun CoroutineScope.updateActiveTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateActiveTable() job started" }

        data.filterNotNull()
            .map { it.map(KunaActive::toEntity) }
            .collectLatest { list ->
                Result.runCatching { dao.save(list) }
                    .onSuccess { Log.info(tag = TAG) { "BalanceTable updated" } }
                    .onFailure { Log.error(tag = TAG, throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateActiveTable() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "ActivePullService"
    }
}

private fun KunaActive.toEntity(): ActiveEntity = ActiveEntity(id, type, quantity, executedQuantity,
    cumulativeQuoteQty, cost, side, pair, price, status, createdAt, updatedAt)