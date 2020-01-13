package com.dpoints.dpointsmerchant.datasource.remote.transaction

data class TransactionModel(
    val message: String,
    val data: List<Tran>
)