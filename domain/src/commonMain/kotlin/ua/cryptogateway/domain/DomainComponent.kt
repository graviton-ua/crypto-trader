package ua.cryptogateway.domain

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import ua.cryptogateway.domain.services.*

interface DomainComponent {
    @Provides
    @IntoSet
    fun provideBalancePullService(impl: BalancePullService): ServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideOhlcvPullService(impl: OhlcvPullService): ServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideTickersPullService(impl: TickersPullService): ServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideActiveOrdersPullService(impl: ActiveOrdersPullService): ServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideTradeBookPullService(impl: TradeBookPullService): ServiceInitializer = impl

    @Provides
    @IntoSet
    fun provideHistoryPullService(impl: HistoryPullService): ServiceInitializer = impl
}