package com.dpoint.dpointsuser.datasource.remote.userdata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MyGift(
    val amount: String,
    val created_at: String,
    val gift_card_id: Int,
    val id: Int,
    val number_of_units: Int,
    val rand_text: String,
    val shop_id: Int,
    val title: String,
    val unit: String,
    val updated_at: String,
    val expired_at: String,
    val user_id: Int
):Parcelable