package com.dpoint.dpointsuser.datasource.remote.auth

import com.squareup.moshi.Json

data class RefreshToken(
    val token: String,
    @field: Json(name = "refresh_token")  val refreshToken: String

)
