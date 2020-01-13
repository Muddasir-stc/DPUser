package com.dpoints.view.module.shops

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import com.bumptech.glide.Glide
import com.dpoint.dpointsuser.R
import com.dpoints.dpointsmerchant.datasource.remote.shop.Shop
import com.dpoints.dpointsmerchant.datasource.remote.shop.ShopModel
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.view.adapter.OfferAdapter
import kotlinx.android.synthetic.main.activity_shop_detail.*

class ShopDetailActivity : BaseActivity() {
    override val layout: Int= R.layout.activity_shop_detail
lateinit var shop:Shop
lateinit var offers:RecyclerView


    override fun init() {
        if(intent.getParcelableExtra<Shop>("SHOP")!=null){
            shop=intent.getParcelableExtra<Shop>("SHOP")
            Log.e("Shop",shop.title)
        }
        titleTag.setText(shop.title)
        txtTitle.text=shop.title
        txtDesc.text=shop.description
        txtPhone.text=shop.contact
        txtEmail.text=shop.email
        txtAddress.text=shop.address
        Glide.with(this).load(shop.image).into(banner)
        Glide.with(this).load(shop.image).into(img)
        offers=findViewById(R.id.offers)
        offers.setHasFixedSize(true)
        val layoutManager=LinearLayoutManager(this,HORIZONTAL,false)
        offers.layoutManager=layoutManager
//        val adpat= OfferAdapter(this, )
//        offers.adapter=adpat
        val btnBack=findViewById<ImageView>(R.id.backBtn)
        btnBack.setOnClickListener {
            onBackPressed()
        }

    }
}
