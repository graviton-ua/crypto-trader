package ua.cryptogateway.domain.services

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