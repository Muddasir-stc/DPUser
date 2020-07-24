package com.dpoint.dpointsuser.datasource.remote.auth

data class LoginModel(
    val message: String,
    val token: String?=null,
    val user: User?=null,
    val OTP: String?=null
)