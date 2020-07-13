package com.dpoint.dpointsuser.datasource.model

import android.os.Parcelable
import com.dpoint.dpointsuser.datasource.remote.gift.Data
import kotlinx.android.parcel.Parcelize


//
// Created by Admin on 6/7/2020.
// Copyright (c) {2020} EMatrix All rights reserved.
//
@Parcelize
data class GiftCardCategory(
    val category:String?=null,
    val list:ArrayList<Data>?= null
):Parcelable