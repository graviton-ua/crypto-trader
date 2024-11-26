package ua.cryptogateway.domain

import ua.cryptogateway.data.web.KtorComponent
import ua.cryptogateway.domain.observers.ObserveFees

interface ServicesComponent : KtorComponent {
    val observeFees: ObserveFees    // TODO: Remove after testing
}