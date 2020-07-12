package com.dpoint.dpointsuser.datasource.remote.userdata

import com.dpoint.dpointsuser.datasource.remote.shop.MenuModel
import com.dpoint.dpointsuser.datasource.remote.ApiCallbackImpl
import com.dpoint.dpointsuser.datasource.remote.ApiClient
import com.dpoint.dpointsuser.datasource.remote.ApiResult
import com.dpoint.dpointsuser.datasource.remote.CallbackImpl
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
        user_id: Int,
        title: String,
        image: String,
        ext:String,
        apiCallbackImpl: ApiCallbackImpl<MenuModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.addMemberShipCard(
            "Bearer $token",
            user_id,
            title,
            image,
            ext
        )
        call.enqueue(CallbackImpl(apiCallbackImpl))
    }

    private interface Service {
        @DELETE("membershipCard/{id}")
        fun deleteMemberShipCard(
            @Header("Authorization") token: String,
            @Path("id") id: String
        ): Call<ApiResult<MenuModel>>

        @GET("membershipCard")
        fun getMemberShipCard(@Header("Authorization") token: String): Call<ApiResult<MenuModel>>

        @POST("membershipCard")
        @FormUrlEncoded
        fun addMemberShipCard(
            @Header("Authorization") token: String,
            @Field("user_id") user_id: Int,
            @Field("title") title: String,
            @Field("image") image: String,
            @Field("ext")ext:String
        ): Call<ApiResult<MenuModel>>
    }
}