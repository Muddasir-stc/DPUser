package com.dpoint.dpointsuser.view.module.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.datasource.remote.history.ExchangeModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftService
import com.dpoints.dpointsmerchant.datasource.remote.gift.HistoryService
import com.dpoints.dpointsmerchant.utilities.Event

class HistoryViewModel:ViewModel() {
    private val TAG = HistoryViewModel::class.qualifiedName

    private val _exchangeHistoryState = MutableLiveData<Event<NetworkState<ExchangeModel>>>()
    val exchangeHistoryState: LiveData<Event<NetworkState<ExchangeModel>>> get() = _exchangeHistoryState

    fun getExchanges(token: String) {
        _exchangeHistoryState.value = Event(NetworkState.Loading())
        HistoryService.instance.getExchanges(token,
            object : ApiCallbackImpl<ExchangeModel>(_exchangeHistoryState) {
                override fun onSuccess(success: ExchangeModel?) {
                    Log.e(TAG, success.toString())
                    _exchangeHistoryState.value = Event(NetworkState.Success(success))
                }

            })
    }
}