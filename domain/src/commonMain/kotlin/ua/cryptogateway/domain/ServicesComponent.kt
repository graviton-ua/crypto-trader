package ua.cryptogateway.domain

import ua.cryptogateway.data.web.KtorComponent
import ua.cryptogateway.domain.observers.ObserveFees
import ua.cryptogateway.domain.observers.ObserveMe

interface ServicesComponent : KtorComponent {
    val observeFees: ObserveFees    // TODO: Remove after testing
    val observeMe: ObserveMe    // TODO: Remove after testing
}