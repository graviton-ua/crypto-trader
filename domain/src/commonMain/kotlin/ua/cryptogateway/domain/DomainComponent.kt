package ua.cryptogateway.domain

import ua.cryptogateway.domain.services.ActiveOrdersPullService
import ua.cryptogateway.domain.services.BalancePullService
import ua.cryptogateway.domain.services.TickersPullService
import ua.cryptogateway.domain.services.TradeBookPullService

interface DomainComponent {
    val balancePuller: BalancePullService
    val tickersPuller: TickersPullService
    val activeOrdersPuller: ActiveOrdersPullService
    val tradeBookPuller: TradeBookPullService
}