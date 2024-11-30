package ua.cryptogateway.domain

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlin.system.measureTimeMillis
import kotlin.time.Duration

internal class DataPuller() {
    @OptIn(DelicateCoroutinesApi::class)
    fun <T> pull(delay: Duration, task: suspend () -> T): Flow<T> {
        return channelFlow {
            while (!isClosedForSend) {
                val elapsedTime = measureTimeMillis {
                    val data = task()
                    if (!isClosedForSend) send(data) else return@channelFlow
                }

                // Calculate remaining delay to maintain consistent interval
                val delayTime = (delay.inWholeMilliseconds - elapsedTime).coerceAtLeast(0)
                delay(delayTime)
            }
        }
    }
}