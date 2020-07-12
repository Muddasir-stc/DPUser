package com.dpoint.dpointsuser.datasource.remote.gift

import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.ApiClient
import com.dpoint.dpointsuser.datasource.remote.ApiResult
import com.dpoint.dpointsuser.datasource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class GiftService private constructor() {
    companion object {
        val instance: GiftService by lazy { GiftService() }
    }

    fun getGiftCards(
        token: String,
        callback: ApiCallbackImpl<GiftModel>
    ) {
        val service = ApiClient.retrofit.create(GiftService.Service::class.java)
        val call = service.getGiftCards("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }



//    fun getGifts(
//        token: String,
//        callback: ApiCallbackImpl<GiftModel>
//    ) {
//        val service = ApiClient.retrofit.create(Service::class.java)
//        val call = service.getGiftCards("Bearer $token")
//        call.enqueue(CallbackImpl(callback))
//    }

    private interface Service {
        @GET("getAllGiftCards")
        fun getGiftCards(
            @Header("Authorization") token: String
        ): Call<ApiResult<GiftModel>>
    }
}