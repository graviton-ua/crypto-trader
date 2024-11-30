package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.dao.TickersDao
import ua.cryptogateway.data.models.db.TickerEntity
import ua.cryptogateway.data.models.web.KunaTicker
import ua.cryptogateway.data.web.api.KunaApi
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
    private val dao: TickersDao,
) {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(10.seconds)
    private var job: Job? = null
    private val data = MutableStateFlow<List<KunaTicker>?>(null)


    init {
        //start()
        scope.updateTickersTable()
    }


    fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "DataPuller job started" }
            DataPuller().pull(delay.value) { api.getTickers() }
                .mapNotNull { it.getOrNull() }
                .catch { Log.error(tag = TAG, throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.value = it
                    Log.debug(tag = TAG) { it.joinToString("\n") }
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


    private fun CoroutineScope.updateTickersTable() = launch(dispatcher) {
        Log.debug(tag = TAG) { "updateTickersTable() job started" }

        data.filterNotNull()
            .map { it.map(KunaTicker::toEntity) }
            .collectLatest { list ->
                dao.save(list)
            }

    }.also { it.invokeOnCompletion { Log.debug(tag = TAG) { "updateTickersTable() job completed (exception: ${it?.message})" } } }


    companion object {
        private const val TAG = "OrdersGridRefreshService"
    }
}

private fun KunaTicker.toEntity(): TickerEntity = TickerEntity(pairName, priceHigh, priceAsk, priceBid, priceLow, priceLast, change, timestamp)