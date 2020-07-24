package com.dpoint.dpointsuser.view.module.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.datasource.remote.history.ExchangeModel
import com.dpoint.dpointsuser.datasource.remote.history.HistoryGift
import com.dpoint.dpointsuser.datasource.remote.history.RedeemModel
import com.dpoints.view.module.gifts.Gifts
import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.gift.HistoryService
import com.dpoint.dpointsuser.utilities.Event

class HistoryViewModel : ViewModel() {
    private val TAG = HistoryViewModel::class.qualifiedName

    private val _exchangeHistoryState = MutableLiveData<Event<NetworkState<ExchangeModel>>>()
    val exchangeHistoryState: LiveData<Event<NetworkState<ExchangeModel>>> get() = _exchangeHistoryState

    private val _redeemHistoryState = MutableLiveData<Event<NetworkState<RedeemModel>>>()
    val redeemHistoryState: LiveData<Event<NetworkState<RedeemModel>>> get() = _redeemHistoryState

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

    private val _historyGiftState = MutableLiveData<Event<NetworkState<GiftModel>>>()
    val historyGiftState: LiveData<Event<NetworkState<GiftModel>>> get() = _historyGiftState

    fun getAllUserUsedGiftCards(token: String) {
        _historyGiftState.value = Event(NetworkState.Loading())
        HistoryService.instance.getAllUserUsedGiftCards(token,
            object : ApiCallbackImpl<GiftModel>(_historyGiftState) {
                override fun onSuccess(success: GiftModel?) {
                    Log.e(TAG, success.toString())
                    _historyGiftState.value = Event(NetworkState.Success(success))
                }

            })
    }

    private val _pointsState = MutableLiveData<Event<NetworkState<ExchangeModel>>>()
    val pointsState: LiveData<Event<NetworkState<ExchangeModel>>> get() = _pointsState

    fun getPoints(token: String) {
        _pointsState.value = Event(NetworkState.Loading())
        HistoryService.instance.getPoints(token,
            object : ApiCallbackImpl<ExchangeModel>(_pointsState) {
                override fun onSuccess(success: ExchangeModel?) {
                    Log.e(TAG, success.toString())
                    _pointsState.value = Event(NetworkState.Success(success))
                }

            })
    }

    fun getRedeems(token: String) {
        _exchangeHistoryState.value = Event(NetworkState.Loading())
        HistoryService.instance.getRedeems(token,
            object : ApiCallbackImpl<RedeemModel>(_redeemHistoryState) {
                override fun onSuccess(success: RedeemModel?) {
                    Log.e(TAG, success.toString())
                    _redeemHistoryState.value = Event(NetworkState.Success(success))
                }

            })
    }
}