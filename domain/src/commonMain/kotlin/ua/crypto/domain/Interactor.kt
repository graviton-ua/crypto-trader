package ua.crypto.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import ua.crypto.core.util.cancellableRunCatching
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

interface UserInitiatedParams {
    val isUserInitiated: Boolean
}

abstract class Interactor<in P, R> {
    private val loadingState = MutableStateFlow(State())

    @OptIn(FlowPreview::class)
    val inProgress: Flow<Boolean> by lazy {
        loadingState
            .debounce {
                if (it.ambientCount > 0) {
                    5.seconds
                } else {
                    0.seconds
                }
            }
            .map { (it.userCount + it.ambientCount) > 0 }
            .distinctUntilChanged()
    }

    private fun addLoader(fromUser: Boolean) {
        loadingState.update {
            if (fromUser) {
                it.copy(userCount = it.userCount + 1)
            } else {
                it.copy(ambientCount = it.ambientCount + 1)
            }
        }
    }

    private fun removeLoader(fromUser: Boolean) {
        loadingState.update {
            if (fromUser) {
                it.copy(userCount = it.userCount - 1)
            } else {
                it.copy(ambientCount = it.ambientCount - 1)
            }
        }
    }

    suspend operator fun invoke(
        params: P,
        timeout: Duration = DefaultTimeout,
        userInitiated: Boolean = params.isUserInitiated,
    ): Result<R> = cancellableRunCatching {
        addLoader(userInitiated)
        withTimeout(timeout) {
            doWork(params)
        }
    }.also {
        removeLoader(userInitiated)
    }

    private val P.isUserInitiated: Boolean
        get() = (this as? UserInitiatedParams)?.isUserInitiated ?: true

    protected abstract suspend fun doWork(params: P): R

    companion object {
        internal val DefaultTimeout = 5.minutes
    }

    private data class State(val userCount: Int = 0, val ambientCount: Int = 0)
}

suspend operator fun <R> Interactor<Unit, R>.invoke(
    timeout: Duration = Interactor.DefaultTimeout,
) = invoke(Unit, timeout)

abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    suspend fun executeSync(params: P): R = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}

//abstract class PagingInteractor<P : PagingInteractor.Parameters<T>, T : Any> : SubjectInteractor<P, PagingData<T>>() {
//  interface Parameters<T : Any> {
//    val pagingConfig: PagingConfig
//  }
//}

@OptIn(ExperimentalCoroutinesApi::class)
abstract class SubjectInteractor<P : Any, T>(
    distinctInputParam: Boolean = true,
) {
    // Ideally this would be buffer = 0, since we use flatMapLatest below, BUT invoke is not
    // suspending. This means that we can't suspend while flatMapLatest cancels any
    // existing flows. The buffer of 1 means that we can use tryEmit() and buffer the value
    // instead, resulting in mostly the same result.
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    val flow: Flow<T> =
        when (distinctInputParam) {
            true -> paramState.distinctUntilChanged()
            false -> paramState
        }
            .flatMapLatest { createObservable(it) }
            .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract suspend fun createObservable(params: P): Flow<T>
}

abstract class SuspendingWorkInteractor<P : Any, T>(
    distinctInputParam: Boolean = true,
) : SubjectInteractor<P, T>(
    distinctInputParam = distinctInputParam
) {
    override suspend fun createObservable(params: P): Flow<T> = flow {
        emit(doWork(params))
    }

    abstract suspend fun doWork(params: P): T
}