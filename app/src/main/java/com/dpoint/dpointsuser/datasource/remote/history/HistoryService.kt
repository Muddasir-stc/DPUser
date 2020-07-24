package com.dpoint.dpointsuser.datasource.remote.gift

import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.datasource.remote.history.ExchangeModel
import com.dpoint.dpointsuser.datasource.remote.history.HistoryGift
import com.dpoint.dpointsuser.datasource.remote.history.RedeemModel
import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.ApiClient
import com.dpoint.dpointsuser.datasource.remote.ApiResult
import com.dpoint.dpointsuser.datasource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

class HistoryService private constructor() {
    companion object {
        val instance: HistoryService by lazy { HistoryService() }
    }

    fun getExchanges(
        token: String,
        callback: ApiCallbackImpl<ExchangeModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getExchanges("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    fun getPoints(
        token: String,
        callback: ApiCallbackImpl<ExchangeModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getPoints("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    fun getRedeems(
        token: String,
        callback: ApiCallbackImpl<RedeemModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getRedeems("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }
   fun getAllUserUsedGiftCards(
        token: String,
        callback: ApiCallbackImpl<GiftModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getAllUserUsedGiftCards("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    private interface Service {
        @GET("exchange")
        fun getExchanges(
            @Header("Authorization") token: String
        ): Call<ApiResult<ExchangeModel>>

        @GET("getAllUsedExchangeOffers")
        fun getPoints(
            @Header("Authorization") token: String
        ): Call<ApiResult<ExchangeModel>>

        @GET("redeem")
        fun getRedeems(
            @Header("Authorization") token: String
        ): Call<ApiResult<RedeemModel>>

        @GET("getAllUserUsedGiftCards")
        fun getAllUserUsedGiftCards(
            @Header("Authorization") token: String
        ): Call<ApiResult<GiftModel>>
    }
}