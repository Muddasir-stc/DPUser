package com.dpoints.dpointsmerchant.datasource.remote.auth

import com.dpoints.dpointsmerchant.datasource.remote.ApiClient
import com.dpoints.dpointsmerchant.datasource.remote.ApiResult
import com.dpoints.dpointsmerchant.datasource.remote.ErrorCodes.Companion.TOKEN_EXPIRED
import com.dpoints.dpointsmerchant.utilities.fromJson
import okhttp3.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.nio.charset.Charset

class TokenAuthenticator (
    private val customerId: Long,
    private val refreshToken: String
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val responseBody = try {
            response.body().asString().fromJson<ApiResult<*>>()
        } catch (e: Exception) {
            null
        }

        val errorstatus: Int = 1
        if (errorstatus != TOKEN_EXPIRED) return null

        val retrofitResponse = ApiClient.retrofit.create(Service::class.java)
            .refreshToken(customerId, refreshToken)
            .execute()

        retrofitResponse.body()?.let { body ->
            val newRefreshToken = "newRereshToken"
            val newToken = "newToken"
            return response.request()
                .newBuilder()
                .header("x-access-token", newToken)
                .header("refresh-token", newRefreshToken)
                .header("token-status", TOKEN_UPDATED)
                .build()
        }

        return null
    }

    private interface Service {
        @GET("/{id}/refreshToken")
        fun refreshToken(
            @Path("id") customerId: Long,
            @Header("x-access-token") refreshToken: String
        ): Call<ApiResult<RefreshToken>>
    }

    companion object {
        const val TOKEN_UPDATED = "updated"
    }
}

fun ResponseBody?.asString(): String? {
    val source = this?.source()
    source?.request(Long.MAX_VALUE)
    val buffer = source?.buffer()
    return buffer?.clone()?.readString(Charset.forName("UTF-8"))
}