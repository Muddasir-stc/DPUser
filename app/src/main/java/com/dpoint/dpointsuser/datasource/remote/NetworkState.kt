package com.dpoint.dpointsuser.datasource.remote

sealed class NetworkState<out T> {
    class Loading<out T> : NetworkState<T>()
    data class Success<out T>(
        val data: T? = null
    ) : NetworkState<T>()
    data class Error<out T>(
        val message: String,
        val errCode: Int = -1,
        val isSessionExpired: Boolean = false
    ) : NetworkState<T>()
    data class Failure<out T>(
        val throwable: Throwable,
        val errCode: Int = -1
    ) : NetworkState<T>()
}