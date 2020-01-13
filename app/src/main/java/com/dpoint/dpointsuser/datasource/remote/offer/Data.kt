package com.dpoints.dpointsmerchant.datasource.remote.offer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Data(
    val amount: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val merchant_id: Int,
    val offer: String,
    val shop_id: Int,
    val title: String,
    val updated_at: String
):Parcelable