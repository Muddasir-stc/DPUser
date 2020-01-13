package com.dpoints.dpointsmerchant.datasource.remote

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dpoints.dpointsmerchant.utilities.Event

interface ApiCallback<T> {
    fun onFailure(t: Throwable, code: Int = -1)
    fun onError(message: String, code: Int = -1, isSessionExpired: Boolean = false)
    fun onSuccess(success: T? = null)
}

abstract class ApiCallbackImpl<T>   (
    private val state: MutableLiveData<Event<NetworkState<T>>>
) : ApiCallback<T> {

    override fun onFailure(t: Throwable, code: Int) {
        Log.e("ERROR", t.message)
        state.value = Event(NetworkState.Failure(t, code))
    }

    override fun onError(message: String, code: Int, isSessionExpired: Boolean) {
        Log.e("Error",message)
        state.value = Event(NetworkState.Error(message, code, isSessionExpired))
    }
}
