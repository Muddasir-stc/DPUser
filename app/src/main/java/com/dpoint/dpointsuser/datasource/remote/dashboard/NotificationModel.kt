package com.dpoints.dpointsmerchant.datasource.remote.dashboard

import com.dpoints.dpointsmerchant.datasource.remote.offer.Data

data class NotificationModel(
    val `data`: List<Data>,
    val message: String
)