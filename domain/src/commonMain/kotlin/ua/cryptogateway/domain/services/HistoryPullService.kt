package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import saschpe.log4k.logged
import ua.cryptogateway.data.db.dao.HistoryDao
import ua.cryptogateway.data.db.models.HistoryEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaHistory
import ua.cryptogateway.domain.DataPuller
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class HistoryPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val daoHistory: HistoryDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaHistory>?>(null)

    init {
        //start()
        scope.updateHistoryTable()
    }

    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "DataPuller job started" }
            DataPuller().pull(delay.value) { api.geHistory() }
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


    private fun CoroutineScope.updateHistoryTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateHistoryTable() job started" }

        data
            .filterNotNull()
            .also {
                Log.info(tag = TAG) { "Flow: $it" }
            }
            .map { it ->
                Log.info(tag = TAG) { "List: $it" }
                it.map(KunaHistory::toEntity)
            }
            .collectLatest { list ->
                daoHistory.save(list)
                    .onSuccess { Log.info(tag = TAG) { "History updated" } }
                    .onFailure { Log.error(tag = TAG, throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateHistoryTable() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "HistoryPullService"
    }
}

private fun KunaHistory.toEntity(): HistoryEntity =
    HistoryEntity(id, orderId, pair, quantity, price, isTaker, fee, feeCurrency, isBuyer, quoteQuantity, createdAt)