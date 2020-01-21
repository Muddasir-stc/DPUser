package com.dpoints.dpointsmerchant.view.module.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.dashboard.NotificationModel
import com.dpoints.dpointsmerchant.datasource.remote.order.OrderModel
import com.dpoints.dpointsmerchant.datasource.remote.order.OrderService
import com.dpoints.dpointsmerchant.datasource.remote.transaction.TransactionModel
import com.dpoints.dpointsmerchant.datasource.remote.transaction.TransactionService
import com.dpoints.dpointsmerchant.utilities.Event
import com.dpoints.dpointsmerchant.utilities.toJson

class TransactionViewModel : ViewModel() {

    private val _transState = MutableLiveData<Event<NetworkState<TransactionModel>>>()
    val transState: LiveData<Event<NetworkState<TransactionModel>>> get() = _transState

    fun getTransactions(token: String) {

        _transState.value = Event(NetworkState.Loading())

        TransactionService.instance.getTransactions(token,
            object : ApiCallbackImpl<TransactionModel>(_transState) {
                override fun onSuccess(success: TransactionModel?) {
                    Log.e("Shop",success?.message)
                    _transState.value = Event(NetworkState.Success(success))
                }
            })
    }

}