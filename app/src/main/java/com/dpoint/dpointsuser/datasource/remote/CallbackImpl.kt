package com.dpoints.dpointsmerchant.successsource.remote

import android.util.Log
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallback
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.utilities.CONNECTION_ERROR
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class Callback<T, R>(
    private val callback: ApiCallback<R>
) : Callback<ApiResult<T>> {
    override fun onFailure(call: Call<ApiResult<T>>, t: Throwable) {
        Log.e("FALIURE MESSAGE", t.message)
        callback.onFailure(t)
    }

    override fun onResponse(call: Call<ApiResult<T>>, response: Response<ApiResult<T>>) {
        if (!response.isSuccessful && response.errorBody() != null) {

            val errorResult = JSONObject(response.errorBody()?.string())
            Log.e("ERROR", errorResult.toString())
            val message = errorResult.getString("message")
            return callback.onError(
                message, response.code(),
                isSessionExpired(response.code())
            )
        }
        val result = response.body() ?: return callback.onFailure(Throwable(CONNECTION_ERROR))

        val warning = response.headers().get("Warning")
        onResponse(result, warning)
    }

    open fun onResponse(result: ApiResult<T>, warning: String?) {

        when {
            (result.message == null && result.success !=null) -> {
                onSuccess(result.success)
            }
            else ->  callback.onError(
                result.message!!
            )
        }
    }

    abstract fun onSuccess(data: T)
}

open class CallbackImpl<T>(
    private val callback: ApiCallback<T>
) : com.dpoints.dpointsmerchant.successsource.remote.Callback<T, T>(callback) {
    override fun onSuccess(data: T) {
        when {
            else -> callback.onSuccess(data)
        }
    }
}


fun isSessionExpired(code: Int?): Boolean {
    return false
}