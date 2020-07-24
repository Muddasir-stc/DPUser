package com.dpoints.view.module.dashboard

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.view.commons.base.BaseFragment





class BusinessFragment(shop: Shop) : BaseFragment(){


    override val layout: Int=R.layout.fragment_business
    val shop=shop
    override fun init(view: View) {
        val txtPhone=view.findViewById<TextView>(R.id.txtPhone)
        val txtAddress=view.findViewById<TextView>(R.id.txtAddress)
        val txtHours=view.findViewById<TextView>(R.id.txtHours)
        val layoutAddr=view.findViewById<LinearLayout>(R.id.layoutAddr)
        txtPhone.setText(shop.contact)
        txtAddress.setText(shop.address)
        txtHours.setText(shop.business_hours)

        layoutAddr.setOnClickListener {
            try {
                val gmmIntentUri = Uri.parse("geo:${shop.latitude},${shop.longitude}")
                var geoUri = "http://maps.google.com/maps?q=loc:" + shop.latitude + "," + shop.longitude + " (" + shop.shop_name + ")";
                var intent =  Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                context!!.startActivity(intent);


            } catch (e: Exception) {
                onError("Invalid Location Found!")
            }

        }
    }




}