package app.tivi.util

fun interface SetCrashReportingEnabledAction {
    operator fun invoke(enabled: Boolean)
}
