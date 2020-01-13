package com.dpoints.dpointsmerchant.datasource.remote.shop

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Shop(
    val address: String,
    val contact: String,
    val created_at: String,
    val description: String,
    val email: String,
    val id: Int,
    val image: String,
    val merchant_id: Int,
    val title: String,
    val updated_at: String
):Parcelable