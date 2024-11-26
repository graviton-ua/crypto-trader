package ua.cryptogateway.util

fun interface SetCrashReportingEnabledAction {
    operator fun invoke(enabled: Boolean)
}
