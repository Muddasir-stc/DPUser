package com.dpoint.dpointsuser.datasource.remote.history

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Exchange(
    val coin_value: Int,
    val coins: Int,
    val created_at: String,
    val shop_name: String,
    val shop_image: String,
    val id: Int,
    val merchant_id: Int,
    val shop_id: Int,
    val total_amount: Int,
    val updated_at: String,
    val user_id: Int
):Parcelable