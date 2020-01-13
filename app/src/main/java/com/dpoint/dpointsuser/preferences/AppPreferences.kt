package com.dpoints.dpointsmerchant.preferences

import android.content.Context
import androidx.core.content.edit
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream


class AppPreferences private constructor() {
    companion object {
        val instance: AppPreferences by lazy { AppPreferences() }
        private const val PREFERENCE_NAME = "com.dpoints.dpointsmerchant"
        private const val KEY_CACHE_USER_NAME = "cache.user.name"
        private const val KEY_CACHE_NAME = "cache.name"
        private const val KEY_CACHE_PROFILE_PIC = "cache.user.profilePic"
    }

    fun cacheUserName(context: Context, userName: String): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit().apply {
            putString(KEY_CACHE_USER_NAME, userName)
        }

        editor.apply()
        return editor.commit()
    }

    fun getCachedUserName(context: Context) = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        .getString(KEY_CACHE_USER_NAME, "") ?: ""

    fun cacheName(context: Context, name: String): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit().apply {
            putString(KEY_CACHE_NAME, name)
        }

        editor.apply()
        return editor.commit()
    }
    fun getStringImage(bm: Bitmap): String {
        val ba = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, ba)
        val imagebyte = ba.toByteArray()
        return Base64.encodeToString(imagebyte, Base64.DEFAULT)
    }
    fun getCachedName(context: Context) = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        .getString(KEY_CACHE_NAME, "") ?: ""

    fun cacheProfilePicture(context: Context, profilePic: String?): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit().apply {
            putString(KEY_CACHE_PROFILE_PIC, profilePic)
        }

        editor.apply()
        return editor.commit()
    }

    fun getCachedProfilePicture(context: Context) = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        .getString(KEY_CACHE_PROFILE_PIC, "") ?: ""

    fun clear(context: Context) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { clear() }
    }
}



