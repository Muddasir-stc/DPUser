package com.dpoints.dpointsmerchant.utilities

import android.content.Context
import com.squareup.moshi.Types
import java.io.IOException
import java.nio.charset.Charset

inline fun <reified T> loadJSONListFromAsset(context: Context, path: String): List<T>? {
    var json: String? = null
    try {
        val inputStream = context.assets.open(path)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
    }

    if (json == null) return null

    val list = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter = moshi.adapter<List<T>>(list)

    return adapter.fromJson(json)
}