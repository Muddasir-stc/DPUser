package com.dpoint.dpointsuser.datasource.remote.gift

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val merchant_id: Int,
    val shop_id: Int,
    val expired_at: String?="",
    val shop_category_icon: String,
    val shop_category_name: String,
    val amount: String,
    val created_at: String,
    val gift_card_id: Int,
    val id: Int,
    val number_of_units: Int,
    val rand_text: String,
    val title: String,
    val unit: String,
    val updated_at: String,
    val shop_profile_picture: String,
    val user_id: Int,
    val shop_name: String
):Parcelable