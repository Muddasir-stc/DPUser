package com.dpoint.dpointsuser.datasource.remote.history

data class HistoryGiftData(
    val amount: String,
    val created_at: String,
    val gift_card_id: Int,
    val id: Int,
    val number_of_units: Int,
    val rand_text: String,
    val shop_name: String,
    val title: String,
    val unit: String,
    val updated_at: String,
    val user_id: Int
)