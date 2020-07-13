package com.dpoint.dpointsuser.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dpoint.dpointsuser.datasource.model.GiftCardCategory
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoint.dpointsuser.view.module.gifts.GiftsCategoriesFragment
import com.dpoint.dpointsuser.view.module.history.Points
import com.dpoints.view.module.dashboard.*

class HistoryViewPagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
       if(position==0){
           return Points()
       }else if(position==1) {
           return GiftsCategoriesFragment.newInstance("history")
       }else{
           return OfferFragment()
       }
    }

    override fun getCount(): Int {
        return 3

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Points"
            1 -> "Gifts"
            else-> "Offers"
        }
    }
}
