package ua.crypto.core.logs

object Log {
    fun debug(message: String = "", throwable: Throwable? = null) = saschpe.log4k.Log.debug(message = message, throwable = throwable, tag = tag)
    fun debug(throwable: Throwable? = null, message: () -> String) = saschpe.log4k.Log.debug(throwable = throwable, tag = tag, message = message)
    fun info(message: String = "", throwable: Throwable? = null) = saschpe.log4k.Log.info(message = message, throwable = throwable, tag = tag)
    fun info(throwable: Throwable? = null, message: () -> String) = saschpe.log4k.Log.info(throwable = throwable, tag = tag, message = message)
    fun warn(message: String = "", throwable: Throwable? = null) = saschpe.log4k.Log.warn(message = message, throwable = throwable, tag = tag)
    fun warn(throwable: Throwable? = null, message: () -> String) = saschpe.log4k.Log.warn(throwable = throwable, tag = tag, message = message)
    fun error(message: String = "", throwable: Throwable? = null) = saschpe.log4k.Log.error(message = message, throwable = throwable, tag = tag)
    fun error(throwable: Throwable? = null, message: () -> String) = saschpe.log4k.Log.error(throwable = throwable, tag = tag, message = message)

    fun debug(throwable: Throwable? = null, tag: String, message: () -> String) =
        saschpe.log4k.Log.debug(throwable = throwable, tag = tag, message = message)


    private val ANONYMOUS_CLASS = """(\$[\w\d]*)""".toRegex()

    private fun createStackElementTag(element: StackTraceElement): String {
        return ANONYMOUS_CLASS.replace(element.className, "")
    }

    private val tag
        get() = Thread.currentThread().stackTrace
            .firstOrNull {
                it.className != this::class.java.name &&
                        it.className != Log::class.java.name &&
                        it.className != Thread::class.java.name
            }
            ?.let(::createStackElementTag) ?: ""
}