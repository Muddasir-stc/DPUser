package com.dpoint.dpointsuser.datasource.remote

import com.dpoint.dpointsuser.BuildConfig
import com.dpoint.dpointsuser.datasource.remote.auth.TokenAuthenticator
import com.dpoint.dpointsuser.datasource.remote.auth.asString
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.TimeUnit

fun getOkHttpClient(
    connectTimeout: Int = 30,
    readTimeout: Int = 30,
    writeTimeout: Int = 30
): OkHttpClient {

    val builder = OkHttpClient.Builder()
    val dispatcher = Dispatcher()
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    builder.dispatcher(dispatcher)

    builder.connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS)
    builder.readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
    builder.writeTimeout(writeTimeout.toLong(), TimeUnit.SECONDS)
    builder.addInterceptor(loggingInterceptor)

    builder.addInterceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .method(original.method(), original.body())
        chain.proceed(requestBuilder.build())
    }

    if (BuildConfig.DEBUG) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
    }

    return builder.build()
}

fun getOkHttpClient(
    customerId: Long,
    refreshToken: String,
    vararg headers: Pair<String, String>,
    connectTimeout: Int = 30,
    readTimeout: Int = 30,
    writeTimeout: Int = 30
): OkHttpClient {
    val authenticator = TokenAuthenticator(customerId, refreshToken)

    val builder = OkHttpClient.Builder()
        .dispatcher(Dispatcher())
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method(), original.body())

            headers.forEach {
                requestBuilder.addHeader(it.first, it.second)
            }

            val response = chain.proceed(requestBuilder.build())
            if ("updated" == response.request().header("token-status")) {
                val newToken = response.request().header("x-access-token")
                val newRefreshToken = response.request().header("refresh-token")
                if (newToken.isNullOrEmpty() || newRefreshToken.isNullOrEmpty())
                    return@addInterceptor response

                return@addInterceptor response
                    .add("token", newToken)
                    ?.add("refreshToken", newRefreshToken) ?: response
            }

            response
        }
        .connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS)
        .writeTimeout(writeTimeout.toLong(), TimeUnit.SECONDS)
        .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)

    builder.authenticator(authenticator)

    if (BuildConfig.DEBUG) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
    }

    return builder.build()
}

private fun Response.add(key: String, value: String): Response? {
    return try {
        val jsonStr = body().asString() ?: return null
        val jsonObject = JSONObject(jsonStr)
        jsonObject.put(key, value)
        val contentType = body()?.contentType()
        val body = ResponseBody.create(contentType, jsonObject.toString())
        newBuilder().body(body).build()
    } catch (e: JSONException) {
        null
    }
}