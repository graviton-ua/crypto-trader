package ua.cryptogateway.data.db.dao

import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.CryptoDb
import ua.cryptogateway.data.db.models.BotConfigEntity
import ua.cryptogateway.data.models.Order
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class BotConfigsDao(
    dispatchers: AppCoroutineDispatchers,
    db: CryptoDb,
) : SqlDelightDao(dispatcher = dispatchers.io, db = db) {

    suspend fun getAll() = transaction {
        bot_configsQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun get(id: Int) = transaction {
        bot_configsQueries.getForId(id = id, mapper = mapper).executeAsOneOrNull()
    }

    suspend fun get(
        baseAsset: String, quoteAsset: String, side: Order.Side,
    ) = transaction {
        bot_configsQueries.getForUnique(baseAsset = baseAsset, quoteAsset = quoteAsset, side = side, mapper = mapper).executeAsOneOrNull()
    }

    suspend fun getActive(side: Order.Side = Order.Side.Sell) = transaction {
        bot_configsQueries.getActiveForSide(side = side, mapper = mapper).executeAsList()
    }

    suspend fun getActiveTickers(side: Order.Side = Order.Side.Sell) = transaction {
        bot_configsQueries.getActiveForSide(side = side, mapper = mapper).executeAsList()
    }


    suspend fun save(
        id: Int?,
        baseAsset: String, quoteAsset: String, side: Order.Side,
        fond: Double, startPrice: Double, priceStep: Double,
        biasPrice: Double, minSize: Double, orderSize: Int,
        sizeStep: Double, orderAmount: Int, priceForce: Boolean,
        market: Boolean, basePrec: Int, quotePrec: Int, active: Boolean,
    ) = Result.runCatching {
        transaction {
            when (id) {
                null -> bot_configsQueries.saveWithUniques(
                    baseAsset = baseAsset, quoteAsset = quoteAsset, side = side,
                    fond = fond, startPrice = startPrice, priceStep = priceStep, biasPrice = biasPrice,
                    minSize = minSize, orderSize = orderSize, sizeStep = sizeStep, orderAmount = orderAmount,
                    priceForce = priceForce, market = market, basePrec = basePrec, quotePrec = quotePrec,
                    active = active,
                )

                else -> bot_configsQueries.saveWithId(
                    id = id, baseAsset = baseAsset, quoteAsset = quoteAsset, side = side,
                    fond = fond, startPrice = startPrice, priceStep = priceStep, biasPrice = biasPrice,
                    minSize = minSize, orderSize = orderSize, sizeStep = sizeStep, orderAmount = orderAmount,
                    priceForce = priceForce, market = market, basePrec = basePrec, quotePrec = quotePrec,
                    active = active,
                )
            }
        }
    }


    suspend fun delete(
        id: Int,
    ) = Result.runCatching {
        transaction {
            bot_configsQueries.deleteById(id = id)
        }
    }

    suspend fun delete(
        baseAsset: String, quoteAsset: String, side: Order.Side,
    ) = Result.runCatching {
        transaction {
            bot_configsQueries.deleteByUniqueKey(baseAsset = baseAsset, quoteAsset = quoteAsset, side = side)
        }
    }
}


private val mapper: (
    Int, String, String, Order.Side, Double, Double, Double, Double,
    Double, Int, Double, Int, Boolean, Boolean, Int, Int, Boolean,
) -> BotConfigEntity = ::BotConfigEntity