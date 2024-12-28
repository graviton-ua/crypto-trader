package ua.crypto.data.web.excpetions

import kotlinx.io.IOException

class WebSocketNeedRetryException(message: String, cause: Throwable? = null) : IOException(message, cause)