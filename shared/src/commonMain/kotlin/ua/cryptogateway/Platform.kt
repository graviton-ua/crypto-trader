package ua.cryptogateway

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform