package com.dpoint.dpointsuser.datasource.remote.order

data class Order(
    val coins: String,
    val created_at: String,
    val id: Int,
    val merchant_id: Int,
    val offer_amount: String,
    val shop_id: Int,
    val shop_name: String,
    val transaction_title: String,
    val transaction_status: String,
    val type: String,
    val updated_at: String,
    val user_id: Int
)