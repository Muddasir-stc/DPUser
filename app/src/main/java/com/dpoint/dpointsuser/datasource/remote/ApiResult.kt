package com.dpoints.dpointsmerchant.datasource.remote
import com.dpoints.dpointsmerchant.datasource.Error


data class ApiResult<T>(
    val success: T? = null,
    val message: String? = null
)

data class ApiResultError(
    val error:String?=null
)