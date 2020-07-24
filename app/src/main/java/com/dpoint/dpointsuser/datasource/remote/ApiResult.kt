package com.dpoint.dpointsuser.datasource.remote
import com.dpoint.dpointsuser.datasource.Error


data class ApiResult<T>(
    val success: T? = null,
    val message: String? = null
)

data class ApiResultError(
    val error:String?=null
)