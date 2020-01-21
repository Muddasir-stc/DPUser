package com.dpoints.dpointsmerchant.view.module.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.NetworkState
import com.dpoints.dpointsmerchant.datasource.remote.dashboard.NotificationModel
import com.dpoints.dpointsmerchant.datasource.remote.order.OrderModel
import com.dpoints.dpointsmerchant.datasource.remote.order.OrderService
import com.dpoints.dpointsmerchant.utilities.Event
import com.dpoints.dpointsmerchant.utilities.toJson

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