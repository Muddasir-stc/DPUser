package com.dpoint.dpointsuser.datasource.remote.shop

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ShopModel(
    val `data`: List<Shop>,
    val message: String
):Parcelable