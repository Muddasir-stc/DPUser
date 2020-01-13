package com.dpoint.dpointsuser.datasource.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ScanedOffer(
    val amount: String,
    val coin_offer_id: String,
    val coin_offer_title: String,
    val merchant_id: String,
    val offer: String,
    val shop_id: String
):Parcelable