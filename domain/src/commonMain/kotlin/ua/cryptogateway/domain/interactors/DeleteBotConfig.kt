package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class DeleteBotConfig(
    dispatchers: AppCoroutineDispatchers,
    val dao: BotConfigsDao,
) : ResultInteractor<DeleteBotConfig.Params, Result<Unit>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Result<Unit> = withContext(dispatcher) {
        dao.delete(
            baseAsset = params.baseAsset,
            quoteAsset = params.quoteAsset,
            side = params.side,
        )
            .onSuccess { Log.info(tag = TAG) { "Config deleted: $params" } }
            .onFailure { Log.error(tag = TAG, throwable = it) { "Config wasn't deleted" } }
            .map { }
    }


    suspend operator fun invoke(
        baseAsset: String, quoteAsset: String, side: Order.Side,
    ) = executeSync(Params(baseAsset, quoteAsset, side))

    data class Params(
        val baseAsset: String,
        val quoteAsset: String,
        val side: Order.Side,
    )


    companion object {
        private const val TAG = "SaveBotConfig"
    }
}