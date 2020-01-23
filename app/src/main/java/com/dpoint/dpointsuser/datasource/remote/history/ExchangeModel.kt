package com.dpoint.dpointsuser.datasource.remote.history

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ExchangeModel(
    val `data`: List<Exchange>,
    val message: String
):Parcelable