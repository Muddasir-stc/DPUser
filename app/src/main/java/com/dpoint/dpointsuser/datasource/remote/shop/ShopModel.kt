package com.dpoints.dpointsmerchant.datasource.remote.shop

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShopModel(
    val message: String?=null,
    val data: List<Shop>?=null
):Parcelable