package ua.cryptogateway.domain

import ua.cryptogateway.domain.observers.ObserveMe
import ua.cryptogateway.domain.services.BalancePullService
import ua.cryptogateway.domain.services.TickersPullService

interface DomainComponent {
    val observeMe: ObserveMe    // TODO: Remove after testing
    val balancePuller: BalancePullService
    val tickersPuller: TickersPullService
}