package com.dpoints.dpointsmerchant.datasource.remote.auth

import android.util.Log
import com.dpoints.dpointsmerchant.datasource.remote.*
import com.dpoints.dpointsmerchant.successsource.remote.CallbackImpl
import com.facebook.login.Login
import retrofit2.Call
import retrofit2.http.*


class AuthService private constructor() {

    companion object { val instance: AuthService by lazy { AuthService() } }

    fun login(
        email: String,
        password: String,
        deviceId: String,
        callback: ApiCallback<LoginModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.login(email,password,deviceId,"Customer")
        call.enqueue(CallbackImpl(callback))
    }

    fun register(
        name: String,
        last_name: String,
        email: String,
        password: String,
        contact_number: String,
        dob: String,
        city: String,
        referralId: String,
        callback: ApiCallback<LoginModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.register(name, last_name, email, password, password, contact_number, dob, city, "Merchant", referralId)
        call.enqueue(CallbackImpl(callback))
    }

    fun verify(
        email: String,
        deviceId: String,
        otp: String,
        callback: ApiCallback<LoginModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.verify(email, deviceId, otp)
        call.enqueue(CallbackImpl(callback))
    }

    fun forgotPassword(
        email: String,
        callback: ApiCallback<LoginModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.forgotPassword(email)
        call.enqueue(CallbackImpl(callback))
    }


    fun resetPassword(
        email: String,
        password: String,
        otp: String,
        callback: ApiCallback<LoginModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.resetPassword(email, password, password, otp)
        call.enqueue(CallbackImpl(callback))
    }


    fun changePassword(
        token: String,
        oldPassword: String,
        newPassword: String,
        callback: ApiCallback<LoginModel>
    ) {Log.e("ERROR","I AM HERE")
        val service = ApiClient.retrofit.create(Service::class.java)
        Log.e("ERROR","I AM HERE")
        val call = service.changePassword("Bearer $token", oldPassword, newPassword)
        Log.e("ERROR","I AM HERE 2")

        call.enqueue(CallbackImpl(callback))
        Log.e("ERROR","I AM HERE 3")
    }



    fun resendOTP(
        email: String,
        callback: ApiCallback<LoginModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.resendOTP(email)
        call.enqueue(CallbackImpl(callback))
    }
    fun getUser(
        token: String,
        callback: ApiCallback<LoginModel>
    ) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getUser("Bearer $token")
        call.enqueue(CallbackImpl(callback))
    }

    fun getCities(callback: ApiCallback<List<CityModel>>) {
        val service = ApiClient.retrofit.create(Service::class.java)
        val call = service.getCities()
        call.enqueue(CallbackImpl(callback))
    }

    fun socialLogin(providerName: String,
                    providerId: String,
                    name: String,
                    email: String,
                    deviceId: String,
                    callback: ApiCallbackImpl<LoginModel>)
    {
        val service = ApiClient.retrofit.create(Service::class.java)

        val call = service.socialLogin(providerName,providerId,name,email,"Merchant", deviceId)
        call.enqueue(CallbackImpl(callback))
    }


    private interface Service {
        @POST("login")
        @FormUrlEncoded
        fun login(
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("device_id") deviceId: String,
            @Field("role") role: String
        ): Call<ApiResult<LoginModel>>

        @POST("verify/me")
        @FormUrlEncoded
        fun verify(
            @Field("email") email: String,
            @Field("device_id") deviceId: String,
            @Field("otp") otp: String
        ): Call<ApiResult<LoginModel>>

        @POST("register")
        @FormUrlEncoded
        fun register(
            @Field("name") name: String,
            @Field("last_name") last_name: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("password_confirmation") password_confirmation: String,
            @Field("contact_number") contact_number: String,
            @Field("dob") dob: String,
            @Field("city") city: String,
            @Field("role") role: String,
            @Field("referrel_id") referralId: String
        ): Call<ApiResult<LoginModel>>

        @POST("reSendOTP")
        @FormUrlEncoded
        fun resendOTP(
            @Field("email") email: String
        ): Call<ApiResult<LoginModel>>

        @POST("password/create")
        @FormUrlEncoded
        fun forgotPassword(
            @Field("email") email: String
        ): Call<ApiResult<LoginModel>>

        @POST("password/reset")
        @FormUrlEncoded
        fun resetPassword(
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("password_confirmation") password_confirmation: String,
            @Field("token") otp: String
        ): Call<ApiResult<LoginModel>>

        @POST("change-password")
        @FormUrlEncoded
        fun changePassword(
            @Header("Authorization") token: String,
            @Field("current_password") email: String,
            @Field("new_password") password: String
        ): Call<ApiResult<LoginModel>>

        @GET("user")
        fun getUser(
            @Header("Authorization") token: String
        ): Call<ApiResult<LoginModel>>

        @GET("getCities")
        fun getCities(): Call<ApiResult<List<CityModel>>>

        @POST("social/login/google")
        @FormUrlEncoded
        fun socialLogin(
            @Field("provider_name") providerName: String,
            @Field("provider_id") providerId: String,
            @Field("name") name: String,
            @Field("email") email: String,
            @Field("role") role: String,
            @Field("device_id") deviceId: String
        ): Call<ApiResult<LoginModel>>

    }
}