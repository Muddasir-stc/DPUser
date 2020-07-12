package com.dpoint.dpointsuser.datasource.remote.transaction

import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.ApiClient
import com.dpoint.dpointsuser.datasource.remote.ApiResult
import com.dpoint.dpointsuser.datasource.remote.CallbackImpl
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