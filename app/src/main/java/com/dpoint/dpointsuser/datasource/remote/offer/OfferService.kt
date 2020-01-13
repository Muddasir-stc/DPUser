package com.dpoints.dpointsmerchant.datasource.remote.offer

import android.util.Log
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
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

    fun offerDelete(
        token: String,
        offer_id: String,
        callback: ApiCallbackImpl<AddOfferModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.deleteOffer("Bearer $token",offer_id)
        call.enqueue(CallbackImpl(callback))
    }

    fun addOffers(
        token: String,
        merchant_id: String,
        shop_id: String,
        image: String,
        ext: String,
        title: String,
        description: String,
        offer: String,
        amount: String,
        callback: ApiCallbackImpl<AddOfferModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.addOffers(
            "Bearer $token",
            merchant_id,
            shop_id,
            image,
            ext,
            title,
            description,
            offer,
            amount
        )
        call.enqueue(CallbackImpl(callback))
    }

    fun updateOffers(
        token: String,
        offer_id:String,
        merchant_id: String,
        shop_id: String,
        image: String,
        ext: String,
        title: String,
        description: String,
        offer: String,
        amount: String,
        callback: ApiCallbackImpl<AddOfferModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.updateOffers(
            "Bearer $token",
            offer_id,
            merchant_id,
            shop_id,
            image,
            ext,
            title,
            description,
            offer,
            amount
        )
        call.enqueue(CallbackImpl(callback))
    }

    private interface Service {
        @GET("getAllCoinOffers")
        fun getOffers(
            @Header("Authorization") token: String
        ): Call<ApiResult<OfferModel>>

        @POST("coinOffer")
        @FormUrlEncoded
        fun addOffers(
            @Header("Authorization") token: String,
            @Field("merchant_id") merchant_id: String,
            @Field("shop_id") shop_id: String,
            @Field("image") image: String,
            @Field("ext") ext: String,
            @Field("title") title: String,
            @Field("description") description: String,
            @Field("offer") offer: String,
            @Field("amount") amount: String
        ): Call<ApiResult<AddOfferModel>>

        @PUT("coinOffer/{item_id}")
        @FormUrlEncoded
        fun updateOffers(
            @Header("Authorization") token: String,
            @Path("item_id") id: String,
            @Field("merchant_id") merchant_id: String,
            @Field("shop_id") shop_id: String,
            @Field("image") image: String,
            @Field("ext") ext: String,
            @Field("title") title: String,
            @Field("description") description: String,
            @Field("offer") offer: String,
            @Field("amount") amount: String
        ): Call<ApiResult<AddOfferModel>>

        @DELETE("coinOffer/{item_id}")
        fun deleteOffer(
            @Header("Authorization") token: String,
            @Path("item_id") id: String
        ): Call<ApiResult<AddOfferModel>>
    }
}