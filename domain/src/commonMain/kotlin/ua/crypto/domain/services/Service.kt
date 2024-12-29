package ua.crypto.domain.services

import kotlinx.coroutines.flow.StateFlow

interface Service {
    val isRunning: StateFlow<Boolean>

    fun start()
    fun stop()
    fun restart() {
        stop()
        start()
    }
}

interface SyncServiceInitializer : Service
interface TraderServiceInitializer : Service