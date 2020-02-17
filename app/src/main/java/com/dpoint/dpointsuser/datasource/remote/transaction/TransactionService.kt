package com.dpoints.dpointsmerchant.datasource.remote.transaction

import android.util.Log
import com.dpoint.dpointsuser.datasource.remote.transaction.UsedOfferModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class TransactionService private constructor() {

    companion object { val instance: TransactionService by lazy { TransactionService() } }

    fun getTransactions(
        token: String,
        callback: ApiCallbackImpl<UsedOfferModel>
    )
    {

        val service = ApiClient.retrofit.create(TransactionService.Service::class.java)
        val call = service.getTransactions("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }


    private interface Service {
        @GET("getAllUserUsedOffers")
        fun getTransactions(
            @Header("Authorization") token: String
        ): Call<ApiResult<UsedOfferModel>>


    }
}