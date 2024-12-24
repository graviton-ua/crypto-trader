package ua.crypto.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.logs.Log
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.dao.BotConfigsDao
import ua.crypto.data.models.Order
import ua.crypto.domain.ResultInteractor

@Inject
class DeleteBotConfig(
    dispatchers: AppCoroutineDispatchers,
    val dao: BotConfigsDao,
) : ResultInteractor<DeleteBotConfig.Params, Result<Unit>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Result<Unit> = withContext(dispatcher) {
        when (params) {
            is Params.ById -> dao.delete(
                id = params.id,
            )

            is Params.ByUniques -> dao.delete(
                baseAsset = params.baseAsset,
                quoteAsset = params.quoteAsset,
                side = params.side,
            )
        }
            .onSuccess { Log.info { "Config deleted: $params" } }
            .onFailure { Log.error(throwable = it) { "Config wasn't deleted" } }
            .map { }
    }


    suspend fun byId(
        id: Int,
    ) = executeSync(Params.ById(id))

    suspend fun byUniques(
        baseAsset: String, quoteAsset: String, side: Order.Side,
    ) = executeSync(Params.ByUniques(baseAsset, quoteAsset, side))

    sealed interface Params {
        data class ById(val id: Int) : Params
        data class ByUniques(
            val baseAsset: String,
            val quoteAsset: String,
            val side: Order.Side,
        ) : Params
    }
}