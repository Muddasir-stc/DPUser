package com.dpoints.dpointsmerchant.datasource.remote

import com.dpoints.dpointsmerchant.utilities.BASE_URL
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
/**
 * Sairah
 * Created by EResolute on 8/11/2018.
 */
class ApiClient {

    companion object {


        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient(120, 120, 120))
                .build()
        }
        @Volatile
        private var instance: Retrofit? = null
        fun getInstance(
            customerId: Long,
            token: String,
            refreshToken: String,
            connectTimeout: Int = 120,
            readTimeout: Int = 120,
            writeTimeout: Int = 120
        ): Retrofit = instance ?: synchronized(this) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    getOkHttpClient(
                        customerId,
                        refreshToken,
                        "Authorization" to token,
                        connectTimeout = connectTimeout,
                        readTimeout = readTimeout,
                        writeTimeout = writeTimeout
                    )
                )
                .build()
                .also {
                    instance = it
                }
        }
        fun clearInstance() {
            instance = null
        }


    }
}