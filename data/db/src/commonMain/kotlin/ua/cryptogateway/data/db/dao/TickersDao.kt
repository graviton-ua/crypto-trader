package ua.cryptogateway.data.db.dao

import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.data.db.CryptoDb
import ua.cryptogateway.data.db.models.TickerEntity
import ua.cryptogateway.util.AppCoroutineDispatchers

@Inject
class TickersDao(
    dispatchers: AppCoroutineDispatchers,
    db: CryptoDb,
) : SqlDelightDao(dispatcher = dispatchers.io, db = db) {


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