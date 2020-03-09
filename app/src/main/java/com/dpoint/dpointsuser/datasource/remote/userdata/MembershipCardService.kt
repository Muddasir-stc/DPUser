package com.dpoint.dpointsuser.datasource.remote.userdata

import com.dpoint.dpointsuser.datasource.remote.shop.Menu
import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoints.dpointsmerchant.datasource.remote.ApiCallbackImpl
import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import retrofit2.Call
import retrofit2.http.*

class MembershipCardService private constructor() {
    companion object {
        val instance: MembershipCardService by lazy { MembershipCardService() }
    }

    fun deleteMemberShipCard(
        token: String,
        id: String,
        apiCallbackImpl: ApiCallbackImpl<MenuModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.deleteMemberShipCard("Bearer $token", id)
        call.enqueue(CallbackImpl(apiCallbackImpl))
    }

    fun getMemberShipCard(
        token: String,
        apiCallbackImpl: ApiCallbackImpl<MenuModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getMemberShipCard("Bearer $token")
        call.enqueue(CallbackImpl(apiCallbackImpl))
    }

    fun addMemberShipCard(
        token: String,
        menuModel: Menu,
        apiCallbackImpl: ApiCallbackImpl<MenuModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.addMemberShipCard(
            "Bearer $token",
            menuModel.title,
            menuModel.image,
            menuModel.ext
        )
        call.enqueue(CallbackImpl(apiCallbackImpl))
    }

    private interface Service {
        @DELETE("membershipcard/{id}")
        fun deleteMemberShipCard(
            @Header("Authorization") token: String,
            @Path("id") id: String
        ): Call<ApiResult<MenuModel>>

        @GET("membershipcard")
        fun getMemberShipCard(@Header("Authorization") token: String): Call<ApiResult<MenuModel>>

        @POST("membershipcard")
        fun addMemberShipCard(
            @Header("Authorization") token: String,
            @Field("title") title: String,
            @Field("image") image: String,
            @Field("ext") ext: String
        ): Call<ApiResult<MenuModel>>
    }
}