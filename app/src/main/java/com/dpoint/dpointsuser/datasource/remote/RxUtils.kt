package com.dpoints.dpointsmerchant.datasource.remote

import com.dpoints.dpointsmerchant.successsource.remote.isSessionExpired
import com.dpoints.dpointsmerchant.utilities.CONNECTION_ERROR
import com.dpoints.dpointsmerchant.utilities.fromJson
import io.reactivex.functions.Consumer
import retrofit2.HttpException

fun <T> rxErrorHandler(callback: ApiCallback<T>): Consumer<Throwable> {
    return Consumer {
        when(it) {
            is HttpException -> {
                val errorResult = it.response().errorBody()?.string()?.fromJson<ApiResult<*>>()
                val message = when {
                    errorResult?.message != null -> errorResult.message!!
                    else -> it.message() ?: CONNECTION_ERROR
                }
                callback.onError(message, 1,
                    isSessionExpired(1))
            }
            else-> callback.onFailure(Throwable(it))
        }
    }
}