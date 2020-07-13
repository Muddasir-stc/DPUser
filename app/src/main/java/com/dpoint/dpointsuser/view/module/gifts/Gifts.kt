package com.dpoints.view.module.gifts

import android.widget.ImageView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.module.gifts.GiftsCategoriesFragment
import com.dpoints.dpointsmerchant.view.commons.base.BaseActivity

class Gifts : BaseActivity() {
    override val layout: Int = R.layout.activity_gifts
    override fun init() {
        var backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        val fragment = GiftsCategoriesFragment.newInstance("AllGifts")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, fragment).commit()
    }
}
