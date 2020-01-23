package com.dpoint.dpointsuser.view.module.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.datasource.remote.history.ExchangeModel
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGiftModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftService
import com.dpoints.dpointsmerchant.datasource.remote.gift.HistoryService
import com.dpoints.dpointsmerchant.datasource.remote.transaction.UserService
import com.dpoints.dpointsmerchant.utilities.Event

class UserViewModel:ViewModel() {
    private val TAG = UserViewModel::class.qualifiedName

    private val _myGiftState = MutableLiveData<Event<NetworkState<MyGiftModel>>>()
    val myGiftState: LiveData<Event<NetworkState<MyGiftModel>>> get() = _myGiftState

    fun getMyGiftCards(token: String) {
        _myGiftState.value = Event(NetworkState.Loading())
        UserService.instance.getMyGifts(token,
            object : ApiCallbackImpl<MyGiftModel>(_myGiftState) {
                override fun onSuccess(success: MyGiftModel?) {
                    Log.e(TAG, success.toString())
                    _myGiftState.value = Event(NetworkState.Success(success))
                }

            })
    }
}