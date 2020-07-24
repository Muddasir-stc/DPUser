package com.dpoint.dpointsuser.view.module.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.datasource.remote.history.ExchangeModel
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGiftModel
import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.auth.LoginModel
import com.dpoint.dpointsuser.datasource.remote.gift.GiftService
import com.dpoint.dpointsuser.datasource.remote.gift.HistoryService
import com.dpoint.dpointsuser.datasource.remote.transaction.UserService
import com.dpoint.dpointsuser.utilities.Event

class UserViewModel:ViewModel() {
    private val TAG = UserViewModel::class.qualifiedName

    private val _myGiftState = MutableLiveData<Event<NetworkState<MyGiftModel>>>()
    val myGiftState: LiveData<Event<NetworkState<MyGiftModel>>> get() = _myGiftState

    private val _userState = MutableLiveData<Event<NetworkState<LoginModel>>>()
    val userState: LiveData<Event<NetworkState<LoginModel>>> get() = _userState

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

    fun getUser(token: String) {
        _userState.value = Event(NetworkState.Loading())
        UserService.instance.getUser(token,
            object : ApiCallbackImpl<LoginModel>(_userState) {
                override fun onSuccess(success: LoginModel?) {
                    Log.e(TAG, success.toString())
                    _userState.value = Event(NetworkState.Success(success))
                }

            })
    }
}