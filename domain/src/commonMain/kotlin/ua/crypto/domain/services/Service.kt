package ua.crypto.domain.services

interface Service {
    fun start()
    fun stop()
    fun restart() {
        stop()
        start()
    }
}

interface SyncServiceInitializer : Service
interface TraderServiceInitializer : Service