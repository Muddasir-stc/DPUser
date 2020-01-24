package com.dpoints.dpointsmerchant.datasource.remote.gift

import com.dpoint.dpointsuser.datasource.remote.gift.GiftModel
import com.dpoint.dpointsuser.datasource.remote.history.ExchangeModel
import com.dpoint.dpointsuser.datasource.remote.history.RedeemModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallback
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

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
    fun getRedeems(
        token: String,
        callback: ApiCallbackImpl<RedeemModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getRedeems("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    private interface Service {
        @GET("exchange")
        fun getExchanges(
            @Header("Authorization") token: String
        ): Call<ApiResult<ExchangeModel>>
        @GET("redeem")
        fun getRedeems(
            @Header("Authorization") token: String
        ): Call<ApiResult<RedeemModel>>
    }
}