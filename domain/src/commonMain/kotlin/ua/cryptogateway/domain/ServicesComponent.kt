package ua.cryptogateway.domain

import ua.cryptogateway.domain.observers.ObserveMe
import ua.cryptogateway.domain.observers.ObserveOrdersGrid

interface ServicesComponent {
    val observeMe: ObserveMe    // TODO: Remove after testing
    val observeOrdersGrid: ObserveOrdersGrid    // TODO: Remove after testing
}