package com.dpoint.dpointsuser.view.module.dashboard

import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.dpoint.dpointsuser.R
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.view.adapter.HistoryViewPagerAdapter
import com.dpoint.dpointsuser.view.adapter.WalletViewPagerAdapter
import com.dpoint.dpointsuser.view.module.shops_near_me.ShopsNearMeActivity
import com.dpoint.dpointsuser.datasource.remote.offer.Data
import com.dpoint.dpointsuser.view.commons.base.BaseFragment
import com.dpoints.view.adapter.ShopAdapter
import com.google.android.material.tabs.TabLayout

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HistoryFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_history
    lateinit var data: List<Data>
    lateinit var list: List<Shop>
    lateinit var adapter: ShopAdapter
    var viewPager: ViewPager? = null
    var tab_history: TabLayout? = null
    var fragmentAdapter: WalletViewPagerAdapter? =null
    override fun init(view: View) {
        viewPager = view.findViewById<ViewPager>(R.id.viewPager_history)
        tab_history = view.findViewById<TabLayout>(R.id.tabs_history)
        fragmentAdapter = WalletViewPagerAdapter(childFragmentManager)
    }

    override fun onResume() {
        super.onResume()
        viewPager!!.adapter = fragmentAdapter
        //  viewPager_shop.isNestedScrollingEnabled=true
        tab_history!!.setupWithViewPager(viewPager)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ShopsNearMeActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
}