package ua.crypto.domain

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import ua.crypto.domain.services.*

interface DomainComponent {

    //  ===============
    //  Crypto Trader services
    //  ===============

    @Provides
    @IntoSet
    fun provideBalancePullService(impl: BalancePullService): TraderServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideTickersPullService(impl: TickersPullService): TraderServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideActiveOrdersPullService(impl: ActiveOrdersPullService): TraderServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideTradeBookPullService(impl: TradeBookPullService): TraderServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideKunaHistoryPullService(impl: KunaHistoryPullService): TraderServiceInitializer = impl


    //  ===============
    //  Crypto Sync services
    //  ===============

    @Provides
    @IntoSet
    fun provideKunaCandlePullService(impl: KunaCandlePullService): SyncServiceInitializer = impl
}