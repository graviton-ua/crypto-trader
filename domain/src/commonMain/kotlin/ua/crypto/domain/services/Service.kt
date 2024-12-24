package ua.crypto.domain.services

interface Service {
    fun start()
    fun stop()
    fun restart()
}

interface ServiceInitializer : Service {
    override fun restart() {
        stop()
        start()
    }
}