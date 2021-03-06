package com.dpoint.dpointsuser.view.module.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.view.adapter.HistoryViewPagerAdapter
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        var backBtn=findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed()
        }
        val fragmentAdapter = HistoryViewPagerAdapter(supportFragmentManager)
        viewPager_history.adapter = fragmentAdapter
        //  viewPager_shop.isNestedScrollingEnabled=true
        tabs_history.setupWithViewPager(viewPager_history)
    }
}
