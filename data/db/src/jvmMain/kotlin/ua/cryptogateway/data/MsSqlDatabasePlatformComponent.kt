package ua.cryptogateway.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.inject.ApplicationScope
import java.io.File
import javax.sql.DataSource

/**
 * Interface for providing configurations and data sources specific to an SQL Server database platform.
 */
actual interface MsSqlDatabasePlatformComponent {
    @Provides
    @ApplicationScope
    fun provideHikariConfig(): HikariConfig = HikariConfig().apply {
        //jdbcUrl = "jdbc:sqlite:${databaseFile.absolutePath}",
        jdbcUrl =
            "jdbc:sqlserver://localhost:49476;databaseName=kuna;encrypt=true;trustServerCertificate=true;" // Update with your database name
        driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
        username = "kuna" // Replace with your username
        password = "kuna" // Replace with your password
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
