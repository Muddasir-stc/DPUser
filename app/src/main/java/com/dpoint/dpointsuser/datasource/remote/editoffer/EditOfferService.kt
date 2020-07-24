package com.dpoint.dpointsuser.datasource.remote.editoffer

import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.ApiClient
import com.dpoint.dpointsuser.datasource.remote.ApiResult
import com.dpoint.dpointsuser.datasource.remote.CallbackImpl
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