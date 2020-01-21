package com.dpoint.dpointsuser.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoints.view.module.dashboard.BusinessFragment
import com.dpoints.view.module.dashboard.OffersFragment

class ShopViewPagerAdapter(
    fm: FragmentManager,
    shop: Shop
) : FragmentPagerAdapter(fm) {
    var shop=shop
    override fun getItem(position: Int): Fragment {
       if(position==0){
           return BusinessFragment(shop)
       }else {
           return OffersFragment(shop)
       }
    }

    override fun getCount(): Int {
        return 2

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Business"
            else-> "Offers"
        }
    }
}
