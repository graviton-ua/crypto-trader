package ua.cryptogateway.appinitializers

import me.tatarka.inject.annotations.Inject

@Inject
class AppSuspendedInitializers(
    private val initializers: Lazy<Set<AppSuspendedInitializer>>,
) : AppSuspendedInitializer {
    override suspend fun initialize() {
        for (initializer in initializers.value) {
            initializer.initialize()
        }
    }
}
