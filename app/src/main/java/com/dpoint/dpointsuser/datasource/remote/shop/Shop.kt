package com.dpoint.dpointsuser.datasource.remote.shop

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Shop(
    val address: String,
    val business_hours: String,
    val category_name: String,
    val coin_value: Int,
    val contact: String,
    val created_at: String,
    val description: String,
    val email: String,
    val facebook: String,
    val id: Int,
    val instagram: String,
    val latitude: String,
    val longitude: String,
    val map: String,
    val membership_status: String,
    val merchant_id: Int,
    val profile_picture: String,
    val rating: String,
    val shop_category_id: Int,
    val shop_name: String,
    val twitter: String,
    val updated_at: String,
    val website: String
):Parcelable