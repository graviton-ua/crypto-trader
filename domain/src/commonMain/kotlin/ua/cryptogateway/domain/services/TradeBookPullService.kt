package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import saschpe.log4k.logged
import ua.cryptogateway.data.db.dao.KunaListDao
import ua.cryptogateway.data.db.dao.TradeBookDao
import ua.cryptogateway.data.db.models.TradeBookEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaTradesBook
import ua.cryptogateway.domain.DataPuller
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class TradeBookPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val daoTradeBook: TradeBookDao,
    private val kunaListDao: KunaListDao,
) {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaTradesBook>?>(null)

    init {
        //start()
        scope.updateTradesBookTable()
    }

    fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "DataPuller job started" }
            DataPuller().pull(delay.value) {
                // Fetch active pairs form KunaList table first and then use active tickers as input params for fetching TradesBookTable
                val active = kunaListDao.getActiveTickers()
                Log.info(tag = TAG) { "Active tickers: $active" }
                active.flatMap { pair ->
                    api.getTradesBook(pair, 1)
                        .onSuccess { value ->
                            Log.info(tag = TAG) { "getTradesBook: $value" }
                        }
                        .onFailure { exception ->
                            Log.error(tag = TAG, throwable = exception) { "getTradesBook: Some error happen" }
                        }
                        .getOrDefault(emptyList())
                }.ifEmpty { null }
            }
                .filterNotNull()
                .catch { Log.error(tag = TAG, throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.value = it
                    it.logged()
                }
        }.also {
            it.invokeOnCompletion {
                Log.debug(tag = TAG) { "DataPuller job completed (exception: ${it?.message})" }; job = null
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }

    fun restart() {
        stop()
        start()
    }

    private fun CoroutineScope.updateTradesBookTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateTradesBook() job started" }

        data.filterNotNull()
            .map { it.map(KunaTradesBook::toEntity) }
            .collectLatest { list ->
                Result.runCatching { daoTradeBook.save(list) }
                    .onSuccess { Log.info(tag = TAG) { "TradesBook updated" } }
                    .onFailure { Log.error(tag = TAG, throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateTradesBook() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "TradesBookPullService"
    }
}

private fun KunaTradesBook.toEntity(): TradeBookEntity =
    TradeBookEntity(id, pair, quoteQuantity, matchPrice, matchQuantity, side, createdAt)
