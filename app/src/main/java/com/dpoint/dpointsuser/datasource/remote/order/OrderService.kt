package com.dpoint.dpointsuser.datasource.remote.order

import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.ApiClient
import com.dpoint.dpointsuser.datasource.remote.ApiResult
import com.dpoint.dpointsuser.datasource.remote.CallbackImpl
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