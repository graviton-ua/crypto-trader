package ua.crypto.core.settings

import kotlinx.coroutines.flow.StateFlow

interface Preference<T> {
    val defaultValue: T

    val flow: StateFlow<T>
    suspend fun set(value: T)
    suspend fun get(): T
    fun getNotSuspended(): T
}

suspend fun Preference<Boolean>.toggle() = set(!get())
