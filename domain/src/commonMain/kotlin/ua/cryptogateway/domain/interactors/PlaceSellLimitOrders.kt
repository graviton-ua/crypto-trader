package ua.cryptogateway.domain.interactors

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import saschpe.log4k.Log
import ua.cryptogateway.data.db.dao.BalanceDao
import ua.cryptogateway.data.db.dao.BotConfigsDao
import ua.cryptogateway.data.db.dao.OrderDao
import ua.cryptogateway.data.web.api.KunaApi
import ua.cryptogateway.domain.ResultInteractor
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class PlaceSellLimitOrders(
    dispatchers: AppCoroutineDispatchers,
    val api: KunaApi,
    val configsDao: BotConfigsDao,
    val balanceDao: BalanceDao,
    val orderDao: OrderDao,
) : ResultInteractor<PlaceSellLimitOrders.Params, Result<Unit>>() {
    private val dispatcher = dispatchers.io

    override suspend fun doWork(params: Params): Result<Unit> = withContext(dispatcher) {
        val configs = configsDao.getActive().filter { it.side == Order.Side.Sell }

        configs.forEach { config ->
            val balance = balanceDao.getCurrency(currency = config.baseAsset)
                .onFailure { Log.warn(it) { "Can't get balance from table for Currency = ${config.baseAsset}" } }
                .getOrNull() ?: return@forEach

            if (balance.balance == 0.0) return@forEach

            val activeOrders = orderDao.get(side = Order.Side.Sell)
                .onFailure { Log.warn(it) { "Can't get active orders from table" } }
                .getOrNull() ?: return@forEach

            val activeOrdersQuantity = activeOrders.sumOf { it.quantity }
            val quantityLimit = config.fond
            when {
                // We have not enough balance, quit
                balance.entire <= quantityLimit -> {

                }

                // We have enough balance, can create new orders
                balance.entire > quantityLimit -> {


                }
            }
        }

        Result.success(Unit)
    }


    data object Params

    companion object {
        private const val TAG = "PlaceSellLimitOrders"
    }
}