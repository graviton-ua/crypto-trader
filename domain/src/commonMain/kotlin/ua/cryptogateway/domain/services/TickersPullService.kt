package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import saschpe.log4k.logged
import ua.cryptogateway.data.db.dao.KunaListDao
import ua.cryptogateway.data.db.dao.TickersDao
import ua.cryptogateway.data.db.models.TickerEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaTicker
import ua.cryptogateway.domain.DataPuller
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class TickersPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val kunaListDao: KunaListDao,
    private val tickersDao: TickersDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaTicker>?>(null)


    init {
        //start()
        scope.updateTickersTable()
    }


    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "DataPuller job started" }
            DataPuller().pull(delay.value) {
                // Fetch active pairs form KunaList table first and then use active tickers as input params for fetching tickers info
                val active = kunaListDao.getActiveTickers()
                Log.info(tag = TAG) { "Active tickers: $active" }
                api.getTickers(pairs = active.toTypedArray())
            }
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


    private fun CoroutineScope.updateTickersTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateTickersTable() job started" }

        data.filterNotNull()
            .map { it.map(KunaTicker::toEntity) }
            .collectLatest { list ->
                Result.runCatching { tickersDao.save(list) }
                    .onSuccess { Log.info(tag = TAG) { "TickersTable updated" } }
                    .onFailure { Log.error(tag = TAG, throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateTickersTable() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "TickersPullService"
    }
}

private fun KunaTicker.toEntity(): TickerEntity = TickerEntity(pairName, priceHigh, priceAsk, priceBid, priceLow, priceLast, change, timestamp)