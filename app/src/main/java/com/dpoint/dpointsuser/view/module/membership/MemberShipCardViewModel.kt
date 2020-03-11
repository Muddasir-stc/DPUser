package com.dpoint.dpointsuser.view.module.membership

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.shop.Menu
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoint.dpointsuser.datasource.remote.userdata.MembershipCardService
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.utilities.Event

class MemberShipCardViewModel : ViewModel() {
    private val _addMemberShipCardState = MutableLiveData<Event<NetworkState<MenuModel>>>()
    val addMemberShipCardState: LiveData<Event<NetworkState<MenuModel>>> get() = _addMemberShipCardState

    fun addMemberShipCardState(token: String, memberShipCardModel: Menu) {
        _addMemberShipCardState.value = Event(NetworkState.Loading())

        MembershipCardService.instance.addMemberShipCard(token, memberShipCardModel,
            object : ApiCallbackImpl<MenuModel>(_addMemberShipCardState) {
                override fun onSuccess(success: MenuModel?) {
                    Log.e("Data", success?.message)
                    _addMemberShipCardState.value = Event(NetworkState.Success(success))
                }
            })
    }

    private val _getMemberShipCardState = MutableLiveData<Event<NetworkState<MenuModel>>>()
    val getMemberShipCardState: LiveData<Event<NetworkState<MenuModel>>> get() = _getMemberShipCardState

    fun getMemberShipCard(token: String) {
        _getMemberShipCardState.value = Event(NetworkState.Loading())

        MembershipCardService.instance.getMemberShipCard(token,
            object : ApiCallbackImpl<MenuModel>(_getMemberShipCardState) {
                override fun onSuccess(success: MenuModel?) {
                    Log.e("Data", success?.message)
                    _getMemberShipCardState.value = Event(NetworkState.Success(success))
                }
            })
    }

    private val _deleteMemberShipCardState = MutableLiveData<Event<NetworkState<MenuModel>>>()
    val deleteMemberShipCardState: LiveData<Event<NetworkState<MenuModel>>> get() = _deleteMemberShipCardState

    fun deleteMemberShipCard(token: String, id: String) {
        _deleteMemberShipCardState.value = Event(NetworkState.Loading())

        MembershipCardService.instance.deleteMemberShipCard(token, id,
            object : ApiCallbackImpl<MenuModel>(_addMemberShipCardState) {
                override fun onSuccess(success: MenuModel?) {
                    Log.e("Data", success?.message)
                    _deleteMemberShipCardState.value = Event(NetworkState.Success(success))
                }
            })
    }
}