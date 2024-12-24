package ua.crypto.shared

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform