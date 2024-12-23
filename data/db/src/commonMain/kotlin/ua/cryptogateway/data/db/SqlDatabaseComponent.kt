package ua.cryptogateway.data.db

import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.inject.ApplicationScope

expect interface SqlDatabasePlatformComponent

interface SqlDatabaseComponent : SqlDatabasePlatformComponent {

    @ApplicationScope
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseFactory,
    ): Database = factory.build()


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
