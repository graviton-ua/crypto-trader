package ua.cryptogateway.inject

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.appinitializers.AppInitializers
import ua.cryptogateway.data.MsSqlDatabaseComponent
import ua.cryptogateway.data.web.KtorComponent
import ua.cryptogateway.domain.ServicesComponent
import ua.cryptogateway.domain.observers.ObserveFees
import ua.cryptogateway.settings.PreferencesComponent
import ua.cryptogateway.util.AppCoroutineDispatchers
import ua.cryptogateway.logs.LoggerComponent

expect interface SharedPlatformApplicationComponent

interface SharedApplicationComponent :
    SharedPlatformApplicationComponent,
    //TasksComponent,
    //ImageLoadingComponent,
    //TmdbComponent,
    ///TraktComponent,
    //AnalyticsComponent,
    LoggerComponent,
    //NotificationsComponent,
    //PerformanceComponent,
    //PermissionsComponent,
    //PowerControllerComponent,
    PreferencesComponent,
    //LicenseDataComponent,
    //EpisodeBinds,
    //FollowedShowsBinds,
    //PopularShowsBinds,
    //RecommendedShowsBinds,
    //RelatedShowsBinds,
    //SearchBinds,
    //ShowImagesBinds,
    //ShowsBinds,
    //TraktAuthComponent,
    //TraktUsersBinds,
    //TrendingShowsBinds,
    //WatchedShowsBinds,
    ServicesComponent,
    KtorComponent,
    MsSqlDatabaseComponent {

    val initializers: AppInitializers
    val dispatchers: AppCoroutineDispatchers
    //val deepLinker: DeepLinker

    @OptIn(ExperimentalCoroutinesApi::class)
    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        databaseWrite = Dispatchers.IO.limitedParallelism(1),
        databaseRead = Dispatchers.IO.limitedParallelism(4),
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )

    @ApplicationScope
    @Provides
    fun provideApplicationCoroutineScope(
        dispatchers: AppCoroutineDispatchers,
    ): ApplicationCoroutineScope = CoroutineScope(dispatchers.main + SupervisorJob())
}
