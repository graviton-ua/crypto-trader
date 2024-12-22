package ua.cryptogateway.data.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.inject.ApplicationScope
import ua.cryptogateway.settings.TiviPreferences
import java.io.File
import javax.sql.DataSource

/**
 * Interface for providing configurations and data sources specific to an SQL Server database platform.
 */
actual interface MsSqlDatabasePlatformComponent {
    @Provides
    @ApplicationScope
    fun provideHikariConfig(
        tiviPreferences: TiviPreferences,
    ): HikariConfig = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:${tiviPreferences.dbPort.getNotSuspended()}/crypto"
        driverClassName = "org.postgresql.Driver"
        username = "crypto_trader"
        password = "crypto_trader"
        maximumPoolSize = 10
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_READ_COMMITTED"
        validate()
    }

    @Provides
    @ApplicationScope
    fun provideDataSource(
        hikariConfig: HikariConfig,
    ): DataSource = HikariDataSource(hikariConfig)
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