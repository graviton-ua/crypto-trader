package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.domain.models.BotConfigModel
import ua.cryptogateway.domain.models.toModel
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class GetBotConfig(
    dispatchers: AppCoroutineDispatchers,
    val dao: BotConfigsDao,
) : ResultInteractor<GetBotConfig.Params, BotConfigModel?>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): BotConfigModel? = withContext(dispatcher) {
        when (params) {
            is Params.ById -> dao.get(
                id = params.id,
            )

            is Params.ByUniques -> dao.get(
                baseAsset = params.baseAsset,
                quoteAsset = params.quoteAsset,
                side = params.side,
            )
        }?.toModel()
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


    companion object {
        private const val TAG = "SaveBotConfig"
    }
}