package ua.cryptogateway.data

import me.tatarka.inject.annotations.Inject
import org.jetbrains.exposed.sql.Database
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
    private val dataSource: DataSource,
) {
    fun build(): Database = Database.connect(
        datasource = dataSource,
        setupConnection = {},
        databaseConfig = null,
    )
}
