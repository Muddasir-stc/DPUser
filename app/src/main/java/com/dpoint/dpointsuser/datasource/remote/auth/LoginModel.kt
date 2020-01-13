package com.dpoints.dpointsmerchant.datasource.remote.auth

data class LoginModel(
    val message: String,
    val token: String?=null,
    val user: User?=null,
    val OTP: String?=null
)