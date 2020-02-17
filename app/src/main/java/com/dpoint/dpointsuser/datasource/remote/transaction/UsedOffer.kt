package com.dpoint.dpointsuser.datasource.remote.transaction

data class UsedOffer(
    val amount: String,
    val coin_offer_id: Int,
    val coin_offer_title: String,
    val created_at: String,
    val id: Int,
    val merchant_id: Int,
    val offer: String,
    val offer_image: String,
    val shop_description: String,
    val shop_name: String,
    val updated_at: String,
    val user_id: Int
)