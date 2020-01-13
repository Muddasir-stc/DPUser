package com.dpoints.dpointsmerchant.utilities



interface OnItemClickListener {
    fun onItemClick(index: Int, adapter: Int = 1)
}

interface OnUpdateClickListener {
    fun onUpdateClick(position: Int, itemId: String, quantity: Int)
}

interface OnRemoveClickListener {
    fun onRemoveClick(position: Int, itemId: String)
}