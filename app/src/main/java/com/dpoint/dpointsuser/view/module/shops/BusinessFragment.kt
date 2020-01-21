package com.dpoints.view.module.dashboard

import android.view.View
import android.widget.TextView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment


class BusinessFragment(shop: Shop) : BaseFragment(){


    override val layout: Int=R.layout.fragment_business
    val shop=shop
    override fun init(view: View) {
        val txtPhone=view.findViewById<TextView>(R.id.txtPhone)
        val txtAddress=view.findViewById<TextView>(R.id.txtAddress)
        val txtHours=view.findViewById<TextView>(R.id.txtHours)
        txtPhone.setText(shop.contact)
        txtAddress.setText(shop.address)
        txtHours.setText(shop.business_hours)
    }




}