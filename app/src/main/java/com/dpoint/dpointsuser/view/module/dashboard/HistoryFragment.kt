package com.dpoint.dpointsuser.view.module.dashboard

import android.view.View
import androidx.viewpager.widget.ViewPager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.view.adapter.HistoryViewPagerAdapter
import com.dpoints.dpointsmerchant.datasource.remote.offer.Data
import com.dpoints.dpointsmerchant.view.commons.base.BaseFragment
import com.dpoints.view.adapter.ShopAdapter
import com.google.android.material.tabs.TabLayout

class HistoryFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_history
    lateinit var data: List<Data>
    lateinit var list: List<Shop>
    lateinit var adapter: ShopAdapter
    override fun init(view: View) {
        val fragmentAdapter = HistoryViewPagerAdapter(activity!!.supportFragmentManager)
        var viewPager = view.findViewById<ViewPager>(R.id.viewPager_history)
        var tab_history = view.findViewById<TabLayout>(R.id.tabs_history)
        viewPager.adapter = fragmentAdapter
        //  viewPager_shop.isNestedScrollingEnabled=true
        tab_history.setupWithViewPager(viewPager)
    }

}