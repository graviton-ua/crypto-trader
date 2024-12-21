package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.logs.Log
import ua.cryptogateway.data.db.dao.BotConfigsDao
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
    private val botConfigsDao: BotConfigsDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaTradesBook>?>(null)

    init {
        //start()
        scope.updateTradesBookTable()
    }

    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug { "DataPuller job started" }
            DataPuller().pull(delay.value) {
                // Fetch active pairs form KunaList table first and then use active tickers as input params for fetching TradesBookTable
                val active = botConfigsDao.getActiveTickers()
//                Log.info { "Active tickers: $active" }
                active.flatMap { pair ->
                    api.getTradesBook(pair, 1)
//                        .onSuccess { value ->
//                            Log.info { "getTradesBook: $value" }
//                        }
                        .onFailure { exception ->
                            Log.error(throwable = exception) { "getTradesBook: Some error happen" }
                        }
                        .getOrDefault(emptyList())
                }.ifEmpty { null }
            }
                .filterNotNull()
                .catch { Log.error(throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.value = it
                }
        }.also {
            it.invokeOnCompletion {
                Log.debug { "DataPuller job completed (exception: ${it?.message})" }; job = null
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateTradesBookTable() = launch(dispatcher) {
        Log.debug { "updateTradesBook() job started" }

        data
            .filterNotNull()
//            .also {
//                Log.info { "Flow: $it" }
//            }
            .map { it ->
//                Log.info { "List: $it" }
                it.map(KunaTradesBook::toEntity)
            }
            .collectLatest { list ->
                daoTradeBook.save(list)
//                    .onSuccess { Log.info { "TradesBook updated" } }
                    .onFailure { Log.error(throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug { "updateTradesBook() job completed (exception: ${it?.message})" } } }
}

private fun KunaTradesBook.toEntity(): TradeBookEntity =
    TradeBookEntity(id, pair, quoteQuantity, matchPrice, matchQuantity, side, createdAt)
