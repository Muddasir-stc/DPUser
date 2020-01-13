package com.dpoints.dpointsmerchant.datasource.remote.shop

import android.util.Log
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

    fun addShop(
        token: String,
        merchentId: String,
        title: String,
        desc: String,
        image: String,
        ext: String,
        address: String,
        contact: String,
        email: String,
        apiCallbackImpl: ApiCallbackImpl<ShopModel>
    ) {
        val service = ApiClient.retrofit.create(ShopService.Service::class.java)
        val call = service.addShop("Bearer $token",merchentId,title,desc,image,ext,address,contact,email)
        call.enqueue(CallbackImpl(apiCallbackImpl))
    }

    fun updateShop(
        token: String,
        merchentId: String,
        title: String,
        desc: String,
        image: String,
        ext: String,
        address: String,
        contact: String,
        email: String,
        id: String,
        apiCallbackImpl: ApiCallbackImpl<ShopModel>
    ) {
        Log.e("SHOPID",id)
        val service = ApiClient.retrofit.create(ShopService.Service::class.java)
        val call = service.updateShop("Bearer $token",merchentId,title,desc,image,ext,address,contact,email,id)
        call.enqueue(CallbackImpl(apiCallbackImpl))
    }

    fun deleteShop(
        token: String,
        id: String,
        apiCallbackImpl: ApiCallbackImpl<ShopModel>
    ) {
        val service = ApiClient.retrofit.create(ShopService.Service::class.java)
        val call = service.deleteShop("Bearer $token",id)
        call.enqueue(CallbackImpl(apiCallbackImpl))
    }


    private interface Service {
        @GET("getAllShopsForUser")
        fun getShops(
            @Header("Authorization") token: String
        ): Call<ApiResult<ShopModel>>
        @POST("shops")
        @FormUrlEncoded
        fun addShop(
            @Header("Authorization") token: String,
            @Field("merchant_id") merchentId: String,
            @Field("title")title: String,
            @Field("description")desc: String,
            @Field("image")image: String,
            @Field("ext")ext: String,
            @Field("address")address: String,
            @Field("contact")contact: String,
            @Field("email")email: String
        ): Call<ApiResult<ShopModel>>

        @PUT("shops/{id}")
        @FormUrlEncoded
        fun updateShop(
            @Header("Authorization") token: String,
            @Field("merchant_id") merchentId: String,
            @Field("title")title: String,
            @Field("description")desc: String,
            @Field("image")image: String,
            @Field("ext")ext: String,
            @Field("address")address: String,
            @Field("contact")contact: String,
            @Field("email")email: String,
            @Path("id")id:String
        ): Call<ApiResult<ShopModel>>

        @DELETE("shops/{id}")
        fun deleteShop(
            @Header("Authorization") token: String,
            @Path("id")id:String
        ): Call<ApiResult<ShopModel>>
    }
}