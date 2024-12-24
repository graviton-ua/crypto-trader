package ua.crypto.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.models.TickerEntity
import ua.crypto.data.sql.Database

@Inject
class TickersDao(
    dispatchers: AppCoroutineDispatchers,
    db: Database,
) : Dao(dispatcher = dispatchers.io, db = db) {


    suspend fun getAll(): List<TickerEntity> = transaction {
        tickersQueries.getAll(mapper = mapper).executeAsList()
    }

    suspend fun getByPairName(pairName: String): TickerEntity? = transaction {
        tickersQueries.getForPairName(pairName = pairName, mapper = mapper).executeAsOneOrNull()
    }


    suspend fun save(tickers: List<TickerEntity>) = Result.runCatching {
        transaction {
            tickers.forEach {
                tickersQueries.save(
                    pairName = it.pairName,
                    priceHigh = it.priceHigh,
                    priceAsk = it.priceLow,
                    priceBid = it.priceBid,
                    priceLow = it.priceLow,
                    priceLast = it.priceLast,
                    change = it.change,
                )
            }
        }
    }
}

private val mapper: (String, Double, Double, Double, Double, Double, Double, Instant) -> TickerEntity = ::TickerEntity