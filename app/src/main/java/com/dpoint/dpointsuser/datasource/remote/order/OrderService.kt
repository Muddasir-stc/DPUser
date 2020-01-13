package com.dpoints.dpointsmerchant.datasource.remote.order

import android.util.Log
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class OrderService private constructor() {

    companion object { val instance: OrderService by lazy { OrderService() } }

    fun getOrders(
        token: String,
        callback: ApiCallbackImpl<OrderModel>
    )
    {

        val service = ApiClient.retrofit.create(OrderService.Service::class.java)
        val call = service.getOrders("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }


    private interface Service {
        @GET("getAllOrdersForSingleUser")
        fun getOrders(
            @Header("Authorization") token: String
        ): Call<ApiResult<OrderModel>>


    }
}