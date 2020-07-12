package com.dpoint.dpointsuser.view.module.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.transaction.UsedOfferModel
import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.transaction.TransactionService
import com.dpoint.dpointsuser.utilities.Event

class TransactionViewModel : ViewModel() {

    private val _transState = MutableLiveData<Event<NetworkState<UsedOfferModel>>>()
    val transState: LiveData<Event<NetworkState<UsedOfferModel>>> get() = _transState

    fun getTransactions(token: String) {

        _transState.value = Event(NetworkState.Loading())

        TransactionService.instance.getTransactions(token,
            object : ApiCallbackImpl<UsedOfferModel>(_transState) {
                override fun onSuccess(success: UsedOfferModel?) {
                    Log.e("Shop", success?.message)
                    _transState.value = Event(NetworkState.Success(success))
                }
            })
    }

}