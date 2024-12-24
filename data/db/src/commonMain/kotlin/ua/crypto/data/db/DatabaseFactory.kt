package ua.crypto.data.db

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import me.tatarka.inject.annotations.Inject
import ua.crypto.data.db.adapters.InstantTimestampColumnAdapter
import ua.crypto.data.sql.*
import javax.sql.DataSource

/**
 * A factory class responsible for creating and configuring instances of [Database] using the provided [DataSource].
 *
 * @constructor
 * Creates an instance of [DatabaseFactory] with the specified [DataSource].
 *
 * @param dataSource The [DataSource] used to establish connections to the database.
 *
 * @inject
 * This class is intended to be used with a dependency injection framework, as indicated by the `@Inject` annotation.
 */
@Inject
class DatabaseFactory(
    private val driver: SqlDriver,
) {
    fun build(): Database = Database(
        driver = driver,
        balanceAdapter = Balance.Adapter(
            updated_atAdapter = InstantTimestampColumnAdapter,
        ),
        tickersAdapter = Tickers.Adapter(
            updated_atAdapter = InstantTimestampColumnAdapter,
        ),
        candlesAdapter = Candles.Adapter(
            open_timeAdapter = InstantTimestampColumnAdapter,
            close_timeAdapter = InstantTimestampColumnAdapter,
            updated_atAdapter = InstantTimestampColumnAdapter,
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
