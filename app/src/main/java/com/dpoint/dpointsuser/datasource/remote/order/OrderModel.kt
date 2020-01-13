package com.dpoints.dpointsmerchant.datasource.remote.order

data class OrderModel(
    val message: String,
    val data: List<Order>
)