package com.esys.mviinitialproject.data.network.api

sealed class ResultWrapper<out T> {
    data class Success<out T : Any>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Exception?) : ResultWrapper<Nothing>()
    object NetworkError : ResultWrapper<Nothing>()
}
