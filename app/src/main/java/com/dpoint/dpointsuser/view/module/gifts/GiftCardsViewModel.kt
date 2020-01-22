package com.dpoint.dpointsuser.view.module.gifts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftService
import com.dpoints.dpointsmerchant.utilities.Event

class GiftCardsViewModel:ViewModel() {
    private val TAG = GiftCardsViewModel::class.qualifiedName

    private val _giftCardState = MutableLiveData<Event<NetworkState<GiftModel>>>()
    val giftCardState: LiveData<Event<NetworkState<GiftModel>>> get() = _giftCardState

    fun getGiftCards(token: String) {
        _giftCardState.value = Event(NetworkState.Loading())
        GiftService.instance.getGiftCards(token,
            object : ApiCallbackImpl<GiftModel>(_giftCardState) {
                override fun onSuccess(success: GiftModel?) {
                    Log.e(TAG, success.toString())
                    _giftCardState.value = Event(NetworkState.Success(success))
                }

            })
    }
}