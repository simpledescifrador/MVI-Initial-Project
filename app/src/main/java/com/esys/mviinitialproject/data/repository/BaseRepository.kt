package com.esys.mviinitialproject.data.repository

import com.esys.mviinitialproject.data.network.api.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    suspend fun <T : Any> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> ResultWrapper<T>,
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                apiCall.invoke()
            } catch (e: Exception) {
                ResultWrapper.NetworkError
            }
        }
    }
}