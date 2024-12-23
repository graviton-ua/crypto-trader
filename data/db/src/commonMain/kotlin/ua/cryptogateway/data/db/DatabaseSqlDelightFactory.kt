package ua.cryptogateway.data.db

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import ua.cryptogateway.data.db.adapters.InstantTimestampColumnAdapter
import javax.sql.DataSource

/**
 * A factory class responsible for creating and configuring instances of [Database] using the provided [DataSource].
 *
 * @constructor
 * Creates an instance of [DatabaseSqlDelightFactory] with the specified [DataSource].
 *
 * @param dataSource The [DataSource] used to establish connections to the database.
 *
 * @inject
 * This class is intended to be used with a dependency injection framework, as indicated by the `@Inject` annotation.
 */
@Inject
class DatabaseSqlDelightFactory(
    private val driver: SqlDriver,
) {
    fun build(): CryptoDb = CryptoDb(
        driver = driver,
        balanceAdapter = Balance.Adapter(
            updated_atAdapter = InstantTimestampColumnAdapter,
        ),
        tickersAdapter = Tickers.Adapter(
            updated_atAdapter = InstantTimestampColumnAdapter,
        ),
        ohlcvAdapter = Ohlcv.Adapter(
            open_timeAdapter = InstantTimestampColumnAdapter,
            close_timeAdapter = InstantTimestampColumnAdapter,
            created_atAdapter = InstantTimestampColumnAdapter,
        ),
        bot_configsAdapter = Bot_configs.Adapter(
            sideAdapter = EnumColumnAdapter(),
        ),
        trade_booksAdapter = Trade_books.Adapter(
            created_atAdapter = InstantTimestampColumnAdapter,
        ),
        historyAdapter = History.Adapter(
            created_atAdapter = InstantTimestampColumnAdapter,
        ),
        ordersAdapter = Orders.Adapter(
            typeAdapter = EnumColumnAdapter(),
            sideAdapter = EnumColumnAdapter(),
            updated_atAdapter = InstantTimestampColumnAdapter,
            created_atAdapter = InstantTimestampColumnAdapter,
        ),
    )
}
