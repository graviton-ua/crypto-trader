package ua.crypto.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.inject.ApplicationCoroutineScope
import ua.crypto.core.inject.ApplicationScope
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.HistoryDao
import ua.crypto.data.db.models.HistoryEntity
import ua.crypto.data.web.api.KunaApi
import ua.crypto.data.web.models.KunaHistory
import ua.crypto.domain.DataPuller
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class KunaHistoryPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val daoHistory: HistoryDao,
) : TraderServiceInitializer {
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
            Log.debug { "DataPuller job started" }
            DataPuller().pull(delay.value) { api.geHistory() }
                .mapNotNull { it.getOrNull() }
                .catch { Log.error(throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.value = it
                }
        }.also { it.invokeOnCompletion { Log.debug { "DataPuller job completed (exception: ${it?.message})" }; job = null } }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateHistoryTable() = launch(dispatcher) {
        Log.debug { "updateHistoryTable() job started" }

        data
            .filterNotNull()
            .also {
                Log.info { "Flow: $it" }
            }
            .map { it ->
                Log.info { "List: $it" }
                it.map(KunaHistory::toEntity)
            }
            .collectLatest { list ->
                daoHistory.save(list)
                    .onSuccess { Log.info { "History updated" } }
                    .onFailure { Log.error(throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug { "updateHistoryTable() job completed (exception: ${it?.message})" } } }
}

private fun KunaHistory.toEntity(): HistoryEntity =
    HistoryEntity(id, orderId, pair, quantity, price, isTaker, fee, feeCurrency, isBuyer, quoteQuantity, createdAt)