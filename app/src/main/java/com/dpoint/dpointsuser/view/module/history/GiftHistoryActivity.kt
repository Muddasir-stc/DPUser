package com.dpoint.dpointsuser.view.module.history

import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.gift.Data
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity
import com.dpoints.view.adapter.RedeemAdapter
import kotlinx.android.synthetic.main.activity_gift_history.*

class GiftHistoryActivity : BaseActivity() {
    override val layout: Int
        get() = R.layout.activity_gift_history

    override fun init() {
        var backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        redeemList.setHasFixedSize(true)
        redeemList.layoutManager= LinearLayoutManager(this)

        var giftCards = intent.getParcelableArrayListExtra<Data>("data")
        var shopName = intent.getStringExtra("shopName")
        var data = ArrayList<Data>()
        for(gift in giftCards){
            if(gift.shop_name.equals(shopName,true))
                data.add(gift)
        }
        loadData(data)
    }

    private fun loadData(data: List<Data>?) {
        redeemList.adapter= RedeemAdapter(data!!,this)
    }
}