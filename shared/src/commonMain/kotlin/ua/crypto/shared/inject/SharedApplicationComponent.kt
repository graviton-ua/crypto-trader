package ua.crypto.shared.inject

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import ua.crypto.core.inject.ApplicationCoroutineScope
import ua.crypto.core.inject.ApplicationScope
import ua.crypto.core.logs.LoggerComponent
import ua.crypto.core.settings.PreferencesComponent
import ua.crypto.core.util.AppCoroutineDispatchers
import ua.crypto.data.db.SqlDatabaseComponent
import ua.crypto.data.web.KtorComponent
import ua.crypto.domain.DomainComponent
import ua.crypto.shared.appinitializers.AppInitializers
import ua.crypto.shared.serviceinitializers.SyncServiceInitializers
import ua.crypto.shared.serviceinitializers.TraderServiceInitializers

expect interface SharedPlatformApplicationComponent

interface SharedApplicationComponent :
    SharedPlatformApplicationComponent,
    LoggerComponent,
    PreferencesComponent,
    DomainComponent,
    KtorComponent,
    SqlDatabaseComponent {

    val initializers: AppInitializers
    //val suspendedInitializers: AppSuspendedInitializers

    val traderServices: TraderServiceInitializers
    val syncServices: SyncServiceInitializers

    val dispatchers: AppCoroutineDispatchers

    val appScope: ApplicationCoroutineScope
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
