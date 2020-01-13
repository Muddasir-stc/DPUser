package com.dpoints.dpointsmerchant.datasource.remote.auth

import com.dpoints.dpointsmerchant.utilities.LoginType
import com.squareup.moshi.Json

data class UserModel (
    @field: Json(name = "user_id") val userId: String,
    @field: Json(name = "full_name") val fullName: String?,
    @field: Json(name = "phone_number") val phoneNumber: String,
    @field: Json(name = "device_id") val deviceId: String,
    @field: Json(name = "wallet_balance") val walletBalance: String,
    @field: Json(name = "coin_balance") val coinBalance: String,
    val email: String,
    val role: String,
    var profilePic: String?,
    @field: Json(name = "phone_number_verified") val phoneVerified: Boolean,
    @field: Json(name = "email_verified") val emailVerified: Boolean,
    var loginType: String? = LoginType.CHANCE_COIN

)
