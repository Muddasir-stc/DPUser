package com.dpoint.dpointsuser.datasource.remote.offer

import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.ApiClient
import com.dpoint.dpointsuser.datasource.remote.ApiResult
import com.dpoint.dpointsuser.datasource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class OfferService private constructor() {
    companion object {
        val instance: OfferService by lazy { OfferService() }
    }

    fun getOffers(
        token: String,
        callback: ApiCallbackImpl<OfferModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getOffers("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

//    fun offerDelete(
//        token: String,
//        offer_id: String,
//        callback: ApiCallbackImpl<AddOfferModel>
//    ) {
//        val service = ApiClient.retrofit.create(Service::class.java)
//        val call = service.deleteOffer("Bearer $token",offer_id)
//        call.enqueue(CallbackImpl(callback))
//    }
//
//    fun addOffers(
//        token: String,
//        merchant_id: String,
//        shop_id: String,
//        image: String,
//        ext: String,
//        title: String,
//        description: String,
//        offer: String,
//        amount: String,
//        callback: ApiCallbackImpl<AddOfferModel>
//    ) {
//        val service = ApiClient.retrofit.create(Service::class.java)
//        val call = service.addOffers(
//            "Bearer $token",
//            merchant_id,
//            shop_id,
//            image,
//            ext,
//            title,
//            description,
//            offer,
//            amount
//        )
//        call.enqueue(CallbackImpl(callback))
//    }
//
//    fun updateOffers(
//        token: String,
//        offer_id:String,
//        merchant_id: String,
//        shop_id: String,
//        image: String,
//        ext: String,
//        title: String,
//        description: String,
//        offer: String,
//        amount: String,
//        callback: ApiCallbackImpl<AddOfferModel>
//    ) {
//        val service = ApiClient.retrofit.create(Service::class.java)
//        val call = service.updateOffers(
//            "Bearer $token",
//            offer_id,
//            merchant_id,
//            shop_id,
//            image,
//            ext,
//            title,
//            description,
//            offer,
//            amount
//        )
//        call.enqueue(CallbackImpl(callback))
//    }

    private interface Service {
        @GET("getAllCoinOffers")
        fun getOffers(
            @Header("Authorization") token: String
        ): Call<ApiResult<OfferModel>>
    }

}