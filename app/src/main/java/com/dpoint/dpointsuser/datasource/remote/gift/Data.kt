package com.dpoints.dpointsmerchant.datasource.remote.gift

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Data(
    val id: Int,
    val merchant_id: Int,
    val shop_id: Int,
    val image: String,
    val title: String,
    val description: String,
    val offer: String,
    val coins: String,
    val rand_text: String,
    val created_at: String,
    val updated_at: String
): Parcelable