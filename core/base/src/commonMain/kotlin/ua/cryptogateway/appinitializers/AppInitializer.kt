package ua.cryptogateway.appinitializers

interface AppInitializer {
    fun initialize()
}

interface AppSuspendedInitializer {
    suspend fun initialize()
}
