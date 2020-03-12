package com.dpoint.dpointsuser.datasource.remote.shop

data class Menu(
    val created_at: String,
    val description: String,
    val id: Int,
    val user_id:Int,
    val image: String,
    val ext: String,
    val shop_id: Int,
    val title: String,
    val updated_at: String
)