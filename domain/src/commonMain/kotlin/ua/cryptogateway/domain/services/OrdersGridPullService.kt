package ua.cryptogateway.domain.services

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.service.OrdersGridDao
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.util.AppCoroutineDispatchers
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@ApplicationScope
@Inject
class OrdersGridPullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val service: OrdersGridDao,
) {
    private val dispatcher = dispatchers.io
    private val delay = MutableStateFlow<Duration>(5.seconds)
    private var job: Job? = null

    init {
        start()
    }


    fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug(tag = TAG) { "DataPuller job started" }
            val puller = DataPuller(dispatcher)

            puller.pull(delay.value) { service.readAll() }
                .catch { Log.error(tag = TAG, throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
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

    companion object {
        private const val TAG = "OrdersGridRefreshService"
    }
}

private class DataPuller(
    private val dispatcher: CoroutineDispatcher,
) {
    @OptIn(DelicateCoroutinesApi::class)
    fun <T> pull(delay: Duration, task: suspend () -> T): Flow<T> {
        return channelFlow {
            while (!isClosedForSend) {
                val elapsedTime = measureTimeMillis {
                    val data = task()
                    if (!isClosedForSend) send(data) else return@channelFlow
                }

                // Calculate remaining delay to maintain consistent interval
                val delayTime = (delay.inWholeMilliseconds - elapsedTime).coerceAtLeast(0)
                delay(delayTime)
            }
        }.flowOn(dispatcher)
    }
}