package ua.cryptogateway.data

import me.tatarka.inject.annotations.Provides
import org.jetbrains.exposed.sql.Database
import ua.cryptogateway.inject.ApplicationScope

expect interface SqlDelightDatabasePlatformComponent

interface SqlDelightDatabaseComponent : SqlDelightDatabasePlatformComponent {
    @ApplicationScope
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseFactory,
    ): Database = factory.build()

//    @ApplicationScope
//    @Provides
//    fun bindTiviShowDao(dao: SqlDelightTiviShowDao): TiviShowDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindUserDao(dao: SqlDelightUserDao): UserDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindTrendingDao(dao: SqlDelightTrendingShowsDao): TrendingDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindPopularDao(dao: SqlDelightPopularShowsDao): PopularDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindWatchedShowDao(dao: SqlDelightWatchedShowsDao): WatchedShowDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindFollowedShowsDao(dao: SqlDelightFollowedShowsDao): FollowedShowsDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindSeasonsDao(dao: SqlDelightSeasonsDao): SeasonsDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindEpisodesDao(dao: SqlDelightEpisodesDao): EpisodesDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindRelatedShowsDao(dao: SqlDelightRelatedShowsDao): RelatedShowsDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindEpisodeWatchEntryDao(dao: SqlDelightEpisodeWatchEntryDao): EpisodeWatchEntryDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindLastRequestDao(dao: SqlDelightLastRequestDao): LastRequestDao = dao
//
//    @ApplicationScope
//    @Provides
//    fun bindShowTmdbImagesDao(dao: SqlDelightShowImagesDao): ShowTmdbImagesDao = dao
//
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
