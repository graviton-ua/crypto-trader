package ua.cryptogateway.data

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
import javax.sql.DataSource

@Inject
class DatabaseFactory(
    private val dataSource: DataSource,
) {
    fun build(): Database = Database.connect(
        datasource = dataSource,
        setupConnection = {},
        databaseConfig = null,
    )
}
