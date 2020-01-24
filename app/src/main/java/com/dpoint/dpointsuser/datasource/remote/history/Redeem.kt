package com.dpoint.dpointsuser.datasource.remote.history

data class Redeem(
    val amount: String,
    val created_at: String,
    val gift_card_id: Int,
    val id: Int,
    val number_of_units: String,
    val shop_id: String,
    val shop_name: String,
    val unit: String,
    val updated_at: String,
    val user_gift_card_id: Int,
    val user_gift_card_title: String,
    val user_id: Int
)