package ua.cryptogateway.domain

import ua.cryptogateway.domain.observers.ObserveMe
import ua.cryptogateway.domain.services.TickersPullService

interface ServicesComponent {
    val observeMe: ObserveMe    // TODO: Remove after testing
    val ordersGridPuller: TickersPullService
}