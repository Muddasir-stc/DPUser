package com.dpoints.dpointsmerchant.datasource.remote.gift

import com.dpoints.dpointsmerchant.datasource.remote.ApiCallback
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
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

    fun giftDelete(
        token: String,
        gift_id: String,
        callback: ApiCallbackImpl<GiftModel>
    ) {
        val service = ApiClient.retrofit.create(GiftService.Service::class.java)
        val call = service.deleteGift("Bearer $token",gift_id)
        call.enqueue(CallbackImpl(callback))
    }

    fun addGiftCards(
        token: String,
        merchant_id: String,
        shop_id: String,
        image: String,
        ext: String,
        title: String,
        description: String,
        gift: String,
        coins: String,
        callback: ApiCallbackImpl<GiftModel>
    ) {
        val service = ApiClient.retrofit.create(GiftService.Service::class.java)
        val call = service.addGiftCards(
            "Bearer $token",
            merchant_id,
            shop_id,
            image,
            ext,
            title,
            description,
            gift,
            coins
        )
        call.enqueue(CallbackImpl(callback))
    }

    fun updateGiftCards(
        token: String,
        gift_id:String,
        merchant_id: String,
        shop_id: String,
        image: String,
        ext: String,
        title: String,
        description: String,
        gift: String,
        coins: String,
        callback: ApiCallbackImpl<GiftModel>
    ) {
        val service = ApiClient.retrofit.create(GiftService.Service::class.java)
        val call = service.updateGiftCards(
            "Bearer $token",
            gift_id,
            merchant_id,
            shop_id,
            image,
            ext,
            title,
            description,
            gift,
            coins
        )
        call.enqueue(CallbackImpl(callback))
    }

    fun getGifts(
        token: String,
        callback: ApiCallbackImpl<GiftModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getGiftCards("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    private interface Service {
        @GET("giftCards")
        fun getGiftCards(
            @Header("Authorization") token: String
        ): Call<ApiResult<GiftModel>>

        @POST("giftCards")
        @FormUrlEncoded
        fun addGiftCards(
            @Header("Authorization") token: String,
            @Field("merchant_id") merchant_id: String,
            @Field("shop_id") shop_id: String,
            @Field("image") image: String,
            @Field("ext") ext: String,
            @Field("title") title: String,
            @Field("description") description: String,
            @Field("offer") gift: String,
            @Field("coins") coins: String
        ): Call<ApiResult<GiftModel>>

        @PUT("giftCards/{item_id}")
        @FormUrlEncoded
        fun updateGiftCards(
            @Header("Authorization") token: String,
            @Path("item_id") id: String,
            @Field("merchant_id") merchant_id: String,
            @Field("shop_id") shop_id: String,
            @Field("image") image: String,
            @Field("ext") ext: String,
            @Field("title") title: String,
            @Field("description") description: String,
            @Field("offer") gift: String,
            @Field("coins") coins: String
        ): Call<ApiResult<GiftModel>>

        @DELETE("giftCards/{item_id}")
        fun deleteGift(
            @Header("Authorization") token: String,
            @Path("item_id") id: String
        ): Call<ApiResult<GiftModel>>
    }
}