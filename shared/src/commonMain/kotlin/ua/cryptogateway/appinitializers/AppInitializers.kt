package ua.cryptogateway.appinitializers

import app.tivi.core.perf.Tracer
import me.tatarka.inject.annotations.Inject

@Inject
class AppInitializers(
    private val initializers: Lazy<Set<AppInitializer>>,
    private val tracer: Tracer,
) : AppInitializer {
    override fun initialize() {
        tracer.trace("AppInitializers") {
            for (initializer in initializers.value) {
                initializer.initialize()
            }
        }
    }
}
