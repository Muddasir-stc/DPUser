package com.dpoint.dpointsuser.datasource.remote.gift

data class Data(
    val amount: String,
    val created_at: String,
    val description: String,
    val id: Int,
    val merchant_id: Int,
    val number_of_units: Int,
    val rand_text: String,
    val shop_id: Int,
    val title: String,
    val unit: String,
    val expired_at: String,
    val updated_at: String
)