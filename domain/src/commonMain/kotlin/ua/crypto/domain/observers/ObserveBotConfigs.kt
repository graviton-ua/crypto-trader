package ua.crypto.domain.observers

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.BotConfigsDao
import ua.crypto.data.db.models.BotConfigEntity
import ua.crypto.domain.SubjectInteractor
import ua.crypto.domain.models.BotConfigModel
import ua.crypto.domain.models.toModel
import ua.crypto.domain.puller
import ua.crypto.domain.utils.SingleRunner
import kotlin.time.Duration.Companion.seconds

@Inject
class ObserveBotConfigs(
    dispatchers: AppCoroutineDispatchers,
    private val configsDao: BotConfigsDao,
) : SubjectInteractor<ObserveBotConfigs.Params, List<BotConfigModel>>() {
    private val dispatcher = dispatchers.computation

    // an isolation runner is used to ensure no concurrent requests are made to the remote mediator.
    // it also handles cancelling lower priority calls with higher priority calls.
    private val isolationRunner = SingleRunner(cancelPreviousInEqualPriority = false)
    private val data = MutableStateFlow<List<BotConfigEntity>>(emptyList())
    private val silentRefreshChannel = MutableSharedFlow<Unit>()

    override suspend fun createObservable(params: Params): Flow<List<BotConfigModel>> = channelFlow {
        // Get list of configs from database with some delay (periodicaly)
        launch(dispatcher) {
            puller(delay = params.timer) { configsDao.getAll() }
                .collectLatest { data.value = it }
        }

        // Launch coroutine to listen for upcoming event for silent refresh
        launch(dispatcher) {
            silentRefreshChannel.collectLatest {
                launch(dispatcher) {
                    refresh { data.value = it }
                }
            }
        }

        launch(dispatcher) {
            data
                .map { it.map(BotConfigEntity::toModel) }
                .collectLatest {
                    send(it)
                }
        }

        awaitClose {/* do on cancellation */ }
    }

    private suspend fun refresh(
        onComplete: (List<BotConfigEntity>) -> Unit,
    ) = withContext(dispatcher) {
        isolationRunner.runInIsolation(priority = PRIORITY_REFRESH) {
            // Suspending network load via Retrofit. This doesn't need to be wrapped in a
            // withContext(Dispatcher.IO) { ... } block since Retrofit's Coroutine CallAdapter
            // dispatches on a worker thread.
            val response = configsDao.getAll()
            onComplete(response)
        }
    }


    suspend fun silentRefresh() = silentRefreshChannel.emit(Unit)


    operator fun invoke() = invoke(Params())

    data class Params(val timer: kotlin.time.Duration = 10.seconds)


    companion object {
        private const val PRIORITY_REFRESH = 100
        private const val PRIORITY_LOAD_NEXT = 50
    }
}