package ua.cryptogateway.domain.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.models.BalanceEntity
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.data.web.models.KunaBalance
import ua.cryptogateway.data.web.sockets.ChannelData
import ua.cryptogateway.data.web.sockets.KunaWebSocket
import ua.cryptogateway.data.web.sockets.KunaWebSocket.Channel
import ua.cryptogateway.data.web.sockets.KunaWebSocketResponse
import ua.cryptogateway.inject.ApplicationCoroutineScope
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.logs.Log
import ua.cryptogateway.util.AppCoroutineDispatchers

@ApplicationScope
@Inject
class BalancePullService(
    dispatchers: AppCoroutineDispatchers,
    private val scope: ApplicationCoroutineScope,
    private val api: KunaApi,
    private val webSocket: KunaWebSocket,
    private val dao: BalanceDao,
) : ServiceInitializer {
    private val dispatcher = dispatchers.io
    private var job: Job? = null
    private val data = MutableSharedFlow<List<KunaBalance>?>(
        replay = 0, // No replay; emit only new values
        extraBufferCapacity = 100, // Buffer up to 100 messages
        onBufferOverflow = BufferOverflow.DROP_OLDEST // Drop the oldest message if buffer is full
    )


    init {
        //start()
        scope.updateBalanceTable()
    }


    override fun start() {
        if (job != null) return
        job = scope.launch(dispatcher) {
            Log.debug { "Websocket job started" }

            webSocket.subscribe(Channel.Accounts)  // Subscribe for all tickers

            webSocket.flow()
                .mapNotNull { result ->
                    result
                        .onFailure { Log.warn(throwable = it) }
                        .getOrNull()
                }
                .filterIsInstance(KunaWebSocketResponse.PublishMessage::class)
                .map { it.data.data }
                .filterIsInstance(ChannelData.Accounts::class)
                .catch { Log.error(throwable = it) }
                .flowOn(dispatcher)
                .collectLatest {
                    data.tryEmit(it.data.assets.map { it.toEntity() })
                }
        }.also { it.invokeOnCompletion { Log.debug { "Websocket job completed (exception: ${it?.message})" }; job = null } }

        // On service start fetch initial balance
        scope.launch(dispatcher) {
            api.getBalance().onSuccess { data.tryEmit(it) }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }


    private fun CoroutineScope.updateBalanceTable() = launch(dispatcher) {
        Log.debug { "updateTickersTable() job started" }

        data.filterNotNull()
            .map { it.map(KunaBalance::toEntity) }
            .collect { list ->
                dao.save(list)
                    .onFailure { Log.error(throwable = it) }
            }

    }.also { it.invokeOnCompletion { Log.debug { "updateTickersTable() job completed (exception: ${it?.message})" } } }
}

private fun KunaBalance.toEntity(): BalanceEntity = BalanceEntity(currency, balance, lockBalance, entire, timestamp)
private fun ChannelData.Accounts.Data.Asset.toEntity(): KunaBalance = KunaBalance(asset, balance, lockBalance)