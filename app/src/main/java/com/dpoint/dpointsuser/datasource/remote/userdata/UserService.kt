package com.dpoints.dpointsmerchant.datasource.remote.transaction

import android.util.Log
import com.dpoint.dpointsuser.datasource.remote.userdata.MyGiftModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.datasource.remote.auth.LoginModel
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class UserService private constructor() {

    companion object { val instance: UserService by lazy { UserService() } }

    fun getMyGifts(
        token: String,
        callback: ApiCallbackImpl<MyGiftModel>
    )
    {

        val service = ApiClient.retrofit.create(UserService.Service::class.java)
        val call = service.getMyGifts("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    fun getUser(token: String,
                callback: ApiCallbackImpl<LoginModel>)
    {
        val service = ApiClient.retrofit.create(UserService.Service::class.java)
        val call = service.getUser("Bearer $token")
        call.enqueue(CallbackImpl(callback))

    }


    private interface Service {
        @GET("userGiftCard")
        fun getMyGifts(
            @Header("Authorization") token: String
        ): Call<ApiResult<MyGiftModel>>
        @GET("user")
        fun getUser(
            @Header("Authorization") token: String
        ): Call<ApiResult<LoginModel>>

    }
}