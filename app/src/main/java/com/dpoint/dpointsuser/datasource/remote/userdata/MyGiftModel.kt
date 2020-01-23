package com.dpoint.dpointsuser.datasource.remote.userdata

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class MyGiftModel(
    val `data`: List<MyGift>,
    val message: String
):Parcelable