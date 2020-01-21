package com.dpoints.dpointsmerchant.datasource.remote.shop

import android.util.Log
import com.dpoint.dpointsuser.datasource.remote.offer.AssignModel
import com.dpoint.dpointsuser.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.datasource.remote.dashboard.NotificationModel
import com.dpoints.dpointsmerchant.datasource.remote.gift.GiftModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferModel
import com.dpoints.dpointsmerchant.datasource.remote.offer.OfferService
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class ShopService private constructor() {

    companion object {
        val instance: ShopService by lazy { ShopService() }
    }
    fun getNotifications(
    token: String,
    callback: ApiCallbackImpl<NotificationModel>
    )
    {

        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getNotifications("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }
    fun getShops(
        token: String,
        callback: ApiCallbackImpl<ShopModel>
    ) {
        val service = ApiClient.retrofit.create(ShopService.Service::class.java)
        val call = service.getShops("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    fun assignOffer(
        token: String,
        userId: String,
        merchantId: String,
        shopId: String,
        coinOfferId: String,
        offerTitle: String,
        offer: String,
        ammount: String,
        callback: ApiCallbackImpl<AssignModel>
    ) {
        val service = ApiClient.retrofit.create(ShopService.Service::class.java)
        val call = service.assignOffer(
            "Bearer $token",
            userId,
            merchantId,
            shopId,
            coinOfferId,
            offerTitle,
            offer,
            ammount
        )
        call.enqueue(CallbackImpl(callback))
    }

    fun redeem(
        tokken: String,
        userId: String,
        merchantId: String,
        shopId: String,
        giftCardId: String,
        giftCardTitle: String,
        coins: String,
        offer: String,
        callback: ApiCallbackImpl<AssignModel>
    ) {
        val service = ApiClient.retrofit.create(ShopService.Service::class.java)
        val call = service.redeem(
            "Bearer $tokken",
            userId,
            merchantId,
            shopId,
            giftCardId,
            giftCardTitle,
            coins,
            offer
        )
        call.enqueue(CallbackImpl(callback))
    }
    fun getShopOffers(
        token: String,
        id:String,
        callback: ApiCallbackImpl<OfferModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getShopOffers("Bearer $token",id)
        call.enqueue(CallbackImpl(callback))
    }
    fun getShopGifts(
        token: String,
        id:String,
        callback: ApiCallbackImpl<GiftModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getShopGifts("Bearer $token",id)
        call.enqueue(CallbackImpl(callback))
    }



    private interface Service {
        @GET("getShopOfferDetails")
        fun getShopOffers(
            @Header("Authorization") token: String,
            @Query("id") id:String
        ): Call<ApiResult<OfferModel>>
        @GET("getShopGiftCardDetails")
        fun getShopGifts(
            @Header("Authorization") token: String,
            @Query("id") id:String
        ): Call<ApiResult<GiftModel>>
        @GET("getAllShopsForUser")
        fun getShops(
            @Header("Authorization") token: String
        ): Call<ApiResult<ShopModel>>

        @POST("assign")
        @FormUrlEncoded
        fun assignOffer(
            @Header("Authorization") token: String,
            @Field("user_id") userId: String,
            @Field("merchant_id") merchantId: String,
            @Field("shop_id") shopId: String,
            @Field("coin_offer_id") coinOfferId: String,
            @Field("coin_offer_title") offerTitle: String,
            @Field("offer") offer: String,
            @Field("amount") ammount: String
        ): Call<ApiResult<AssignModel>>

        @POST("redeem")
        @FormUrlEncoded
        fun redeem(
            @Header("Authorization") tokken: String,
            @Field("user_id") userId: String,
            @Field("merchant_id") merchantId: String,
            @Field("shop_id") shopId: String,
            @Field("gift_card_id") coinOfferId: String,
            @Field("gift_card_title") offerTitle: String,
            @Field("coins") offer: String,
            @Field("offer") ammount: String
        ): Call<ApiResult<AssignModel>>
        @GET("getNotifications")
        fun getNotifications(
            @Header("Authorization") token: String
        ): Call<ApiResult<NotificationModel>>
    }
}