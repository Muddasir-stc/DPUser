package com.dpoint.dpointsuser.preferences

import android.content.Context
import androidx.core.content.edit
import com.dpoint.dpointsuser.datasource.remote.auth.User
import com.dpoint.dpointsuser.utilities.fromJson
import com.dpoint.dpointsuser.utilities.toJson


class UserPreferences {

    companion object {
        val instance: UserPreferences by lazy { UserPreferences() }
        private const val USER = "USER"
        private const val POSTS = "POSTS"
        private const val ORDER = "ORDER"
        private const val BUYSELLORDER = "BUYSELLORDER"
        private const val IS_LOGGED_IN = "IS_LOGGED_IN"
        private const val PREFERENCE_NAME = "user.preferences"
        private const val SAVED_TOKEN = "TOKEN"
        private const val REFRESH_TOKEN = "REFRESHTOKEN"
        private const val CHANCE_ID = "CHANCEID"
    }

    fun saveUser(context: Context, user: User) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit {
            putString(USER, user.toJson())
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }

   /* fun saveData(context: Context, post: PostModel) {
        val gson = Gson()
        val data = getData(context) as ArrayList<PostModel>
        data.add(post)
        val json = gson.toJson(data)
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        preferences.putString(POSTS, json)
        preferences.commit()
    }

    fun remove(context: Context, post: PostModel) {
        val gson = Gson()
        val data = getData(context) as ArrayList<PostModel>
        var i = 0
        for (d in data) {
            if (d.id == post.id) {
                data.removeAt(i)
                break
            }
            i++
        }
        val json = gson.toJson(data)
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        preferences.putString(POSTS, json)
        preferences.commit()
    }

    fun getData(context: Context): List<PostModel> {
        val gson = Gson()
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val json = preferences.getString(POSTS, "")
        return if (json.isEmpty()) {
            ArrayList<PostModel>()
        } else {
            val type = object : TypeToken<List<PostModel>>() {

            }.type
            val arrPackageData = gson.fromJson<List<PostModel>>(json, type)
            arrPackageData
        }
    }

    fun saveOrder(context: Context, order: OrderModel) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit {
            putString(ORDER, order.toJson())
            apply()
        }
    }
*/
    fun logout(context: Context) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit().clear().commit()
    }

    fun getUser(context: Context): User? {
        val userStr = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(USER, null)
        return userStr?.fromJson()
    }

    //    fun getPosts(context: Context) : ArrayList<PostModel>? {
//        val post = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
//            .getString(POSTS, null)
//        var gson = Gson()
//        var list: List<PostModel>
//        return if (post == null || post.isEmpty()) {
//            null
//        } else {
//            var type = object : TypeToken<List<PostModel>>() {}.type
//            list = gson.fromJson<ArrayList<PostModel>>(post, type)
//            list
//        }
//    }
//    fun addPost(context: Context, post: PostModel) {
//        var list = getPosts(context)
//        var gson: Gson? = null
//        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
//        preferences.edit {
//            putString(POSTS, gson?.toJson(list))
//            apply()
//        }
//    }
    fun isLoggedIn(context: Context): Boolean {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun clear(context: Context) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit { clear() }
    }
/*

    fun updateProfilePic(context: Context, pic: String?): Boolean {
        val user = getUser(context) ?: return false
        user.profilePic = pic

        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        return preferences.edit().apply {
            putString(USER, user.toJson())
            apply()
        }.commit()

    }
*/

    fun setLoggedIn(context: Context) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit {
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }

    fun saveToken(context: Context, token: String?) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit() {
            putString(SAVED_TOKEN, token)
            apply()
        }
    }

    fun getTokken(context: Context): String? {
        val tokenn = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(SAVED_TOKEN, null)
        return tokenn
    }

    fun saveRefreshToken(context: Context, token: String?) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit() {
            putString(REFRESH_TOKEN, token)
            apply()
        }
    }

    fun getRefreshTokken(context: Context): String? {
        val tokenn = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(REFRESH_TOKEN, null)
        return tokenn
    }

    fun saveChanceId(context: Context, id: String?) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        preferences.edit() {
            putString(CHANCE_ID, id)
            apply()
        }
    }

    fun getChanceId(context: Context): String? {
        val id = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getString(CHANCE_ID, null)
        return id

    }
}