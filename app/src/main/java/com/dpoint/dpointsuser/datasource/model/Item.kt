package com.dpoint.dpointsuser.datasource.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item (
    val name: String,
    val iconRes: Int? = null,
    val imageUrl: String? = null
) : Parcelable