package com.dpoints.dpointsmerchant.datasource.remote.shop

import android.util.Log
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class ShopService private constructor() {

    companion object { val instance: ShopService by lazy { ShopService() } }

    fun getShops(
        token: String,
        callback: ApiCallbackImpl<ShopModel>
    )
    {
        val service = ApiClient.retrofit.create(ShopService.Service::class.java)
        val call = service.getShops("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }


    private interface Service {
        @GET("getAllShopsForUser")
        fun getShops(
            @Header("Authorization") token: String
        ): Call<ApiResult<ShopModel>>
    }
}