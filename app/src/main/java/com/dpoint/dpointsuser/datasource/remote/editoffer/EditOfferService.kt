package com.dpoints.dpointsmerchant.datasource.remote.editoffer

import com.dpoints.dpointsmerchant.datasource.remote.ApiCallback
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

class EditOfferService private constructor() {
    companion object {
        val instance: EditOfferService by lazy { EditOfferService() }
    }

    fun getShops(
        token: String,
        callback: ApiCallbackImpl<ShopModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getShops("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    private interface Service {
        @GET("shops")
        fun getShops(
            @Header("Authorization") token: String
        ): Call<ApiResult<ShopModel>>
    }
}