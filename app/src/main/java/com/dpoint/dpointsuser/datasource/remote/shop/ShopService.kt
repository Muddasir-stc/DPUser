package com.dpoints.dpointsmerchant.datasource.remote.shop

import android.util.Log
import com.dpoint.dpointsuser.datasource.remote.offer.AssignModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class ShopService private constructor() {

    companion object { val instance: ShopService by lazy { ShopService() } }

    fun getShops(
        token: String,
        callback: ApiCallbackImpl<ShopModel>
    )
    {
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
        val call = service.assignOffer("Bearer $token",userId,merchantId,shopId,coinOfferId,offerTitle,offer,ammount)
        call.enqueue(CallbackImpl(callback))
    }


    private interface Service {
        @GET("getAllShopsForUser")
        fun getShops(
            @Header("Authorization") token: String
        ): Call<ApiResult<ShopModel>>

        @POST("assign")
        @FormUrlEncoded
        fun assignOffer(
            @Header("Authorization") token: String,
            @Field("user_id")userId: String,
            @Field("merchant_id")merchantId: String ,
            @Field("shop_id")  shopId: String,
            @Field("coin_offer_id")coinOfferId: String ,
            @Field("coin_offer_title")offerTitle: String ,
            @Field("offer")offer: String ,
            @Field("amount")ammount: String
        ): Call<ApiResult<AssignModel>>
    }
}