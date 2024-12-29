package ua.crypto.shared.serviceinitializers

import me.tatarka.inject.annotations.Inject
import ua.crypto.domain.services.SyncServiceInitializer
import ua.crypto.domain.services.TraderServiceInitializer

@Inject
class TraderServiceInitializers(
    private val initializers: Lazy<Set<TraderServiceInitializer>>,
) {
    fun start() = initializers.value.forEach { it.start() }

    fun stop() = initializers.value.forEach { it.stop() }
}

@Inject
class SyncServiceInitializers(
    private val initializers: Lazy<Set<SyncServiceInitializer>>,
) {
    fun start() = initializers.value.forEach { it.start() }

    fun stop() = initializers.value.forEach { it.stop() }
}