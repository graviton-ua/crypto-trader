package ua.cryptogateway.data.db

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import org.jetbrains.exposed.sql.Database
import ua.cryptogateway.appinitializers.AppSuspendedInitializer
import ua.cryptogateway.inject.ApplicationScope

expect interface SqlDatabasePlatformComponent

interface SqlDatabaseComponent : SqlDatabasePlatformComponent {
    @ApplicationScope
    @Provides
    fun provideMsSqlDatabase(
        factory: DatabaseFactory,
    ): Database = factory.build()

    @ApplicationScope
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseSqlDelightFactory,
    ): CryptoDb = factory.build()


    @Provides
    @IntoSet
    fun provideDbMigrationInitializer(impl: DbMigrationInitializer): AppSuspendedInitializer = impl

//    @ApplicationScope
//    @Provides
//    fun bindRecommendedDao(dao: SqlDelightRecommendedShowsDao): RecommendedDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindLibraryShowsDao(dao: SqlDelightLibraryShowsDao): LibraryShowsDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun provideDatabaseTransactionRunner(runner: SqlDelightTransactionRunner): DatabaseTransactionRunner = runner
}
