package com.dpoints.dpointsmerchant.utilities

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setDate")
fun setDate(view: TextView, date: String) {
    val formattedDate = DateTime.parse(date, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "dd MMM HH:mm")
    view.text = formattedDate
}
