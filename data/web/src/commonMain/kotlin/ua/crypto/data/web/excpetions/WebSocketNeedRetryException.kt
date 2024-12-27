package ua.crypto.data.web.excpetions

class WebSocketNeedRetryException(message: String, cause: Throwable? = null) : IllegalStateException(message, cause)