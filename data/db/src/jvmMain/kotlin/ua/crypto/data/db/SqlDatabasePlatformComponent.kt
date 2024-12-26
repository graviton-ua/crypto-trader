package ua.crypto.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.tatarka.inject.annotations.Provides
import ua.crypto.core.inject.ApplicationScope
import ua.crypto.core.settings.TraderPreferences
import ua.crypto.data.sql.Database
import java.io.File
import javax.sql.DataSource

/**
 * Interface for providing configurations and data sources specific to an SQL Server database platform.
 */
actual interface SqlDatabasePlatformComponent {
    @Provides
    @ApplicationScope
    fun provideHikariConfig(
        traderPreferences: TraderPreferences,
    ): HikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:${traderPreferences.dbPort.getNotSuspended()}/crypto"
        driverClassName = "org.postgresql.Driver"
        username = "crypto_trader"
        password = "crypto_trader"
        maximumPoolSize = 10
        isAutoCommit = true
        transactionIsolation = "TRANSACTION_READ_COMMITTED"
        validate()
    }

    @Provides
    @ApplicationScope
    fun provideDataSource(
        hikariConfig: HikariConfig,
    ): DataSource = HikariDataSource(hikariConfig)


    @Provides
    @ApplicationScope
    fun provideDriverFactory(
        dataSource: DataSource,
    ): SqlDriver = dataSource.asJdbcDriver()
        .also { db ->
            Database.Schema.create(db)
            //db.execute(null, "PRAGMA foreign_keys=ON", 0)
        }
}

private val databaseFile: File
    get() = File(appDir.also { if (!it.exists()) it.mkdirs() }, "tivi.db")

private val appDir: File
    get() {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> {
                File(System.getenv("AppData"), "tivi/db")
            }

            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                File(System.getProperty("user.home"), ".tivi")
            }

            os.contains("mac") -> {
                File(System.getProperty("user.home"), "Library/Application Support/tivi")
            }

            else -> error("Unsupported operating system")
        }
    }