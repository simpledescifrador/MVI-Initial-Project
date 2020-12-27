package com.esys.mviinitialproject.data.repository

import com.esys.mviinitialproject.data.network.api.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

abstract class BaseRepository {
    protected suspend fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> ResultWrapper<T>,
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                Timber.tag("SafeApiResult").d("API Call Invoke: ${apiCall.hashCode()}")
                apiCall.invoke()
            } catch (e: Exception) {
                Timber.tag("SafeApiResultFailed").e(e)
                ResultWrapper.NetworkError
            }
        }
    }

    protected suspend fun <T> retryIO(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 1000,    // 1 second
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                Timber.tag("RetryIO").d("Running...")
                return block()
            } catch (e: IOException) {
                // you can log an error here and/or make a more finer-grained
                // analysis of the cause to see if retry is needed
                Timber.tag("RetryIOError").e(e)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block() // last attempt
    }
}