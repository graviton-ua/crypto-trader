package ua.cryptogateway.serviceinitializers

import me.tatarka.inject.annotations.Inject
import ua.cryptogateway.domain.services.ServiceInitializer

@Inject
class ServiceInitializers(
    private val initializers: Lazy<Set<ServiceInitializer>>,
) : ServiceInitializer {

    override fun start() = initializers.value.forEach { it.start() }

    override fun stop() = initializers.value.forEach { it.stop() }

    override fun restart() = initializers.value.forEach { it.restart() }
}