package ua.crypto.core.appinitializers

interface AppInitializer {
    fun initialize()
}

interface AppSuspendedInitializer {
    suspend fun initialize()
}
