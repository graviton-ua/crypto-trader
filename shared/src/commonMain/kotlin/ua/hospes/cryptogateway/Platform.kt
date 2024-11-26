package ua.hospes.cryptogateway

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform