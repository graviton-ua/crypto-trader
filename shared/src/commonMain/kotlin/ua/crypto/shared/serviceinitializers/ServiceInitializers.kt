package ua.crypto.shared.serviceinitializers

import me.tatarka.inject.annotations.Inject
import ua.crypto.domain.services.SyncServiceInitializer
import ua.crypto.domain.services.TraderServiceInitializer

@Inject
class TraderServiceInitializers(
    private val initializers: Lazy<Set<TraderServiceInitializer>>,
) : TraderServiceInitializer {

    override fun start() = initializers.value.forEach { it.start() }

    override fun stop() = initializers.value.forEach { it.stop() }

    override fun restart() = initializers.value.forEach { it.restart() }
}

@Inject
class SyncServiceInitializers(
    private val initializers: Lazy<Set<SyncServiceInitializer>>,
) : SyncServiceInitializer {

    override fun start() = initializers.value.forEach { it.start() }

    override fun stop() = initializers.value.forEach { it.stop() }

    override fun restart() = initializers.value.forEach { it.restart() }
}