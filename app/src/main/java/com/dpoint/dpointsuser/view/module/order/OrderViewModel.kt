package com.dpoint.dpointsuser.view.module.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.NetworkState
import com.dpoint.dpointsuser.datasource.remote.dashboard.NotificationModel
import com.dpoint.dpointsuser.datasource.remote.order.OrderModel
import com.dpoint.dpointsuser.datasource.remote.order.OrderService
import com.dpoint.dpointsuser.utilities.Event
import com.dpoint.dpointsuser.utilities.toJson

class OrderViewModel : ViewModel() {

    private val _orderState = MutableLiveData<Event<NetworkState<OrderModel>>>()
    val orderState: LiveData<Event<NetworkState<OrderModel>>> get() = _orderState

    fun getOrders(token: String) {

        _orderState.value = Event(NetworkState.Loading())

        OrderService.instance.getOrders(token,
            object : ApiCallbackImpl<OrderModel>(_orderState) {
                override fun onSuccess(success: OrderModel?) {
                    Log.e("Shop",success?.message)
                    _orderState.value = Event(NetworkState.Success(success))
                }
            })
    }

}