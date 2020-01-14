package com.dpoint.dpointsuser.datasource.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ScannedGift(
    val coins: String,
    val gift_card_id: String,
    val gift_card_title: String,
    val merchant_id: String,
    val offer: String,
    val shop_id: String
):Parcelable