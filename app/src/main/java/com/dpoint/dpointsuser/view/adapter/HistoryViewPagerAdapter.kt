package com.dpoint.dpointsuser.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dpoint.dpointsuser.datasource.remote.shop.Shop
import com.dpoints.view.module.dashboard.*

class HistoryViewPagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
       if(position==0){
           return ExchangeFragment()
       }else if(position==1) {
           return OfferFragment()
       }else{
           return GiftcardFragment()
       }
    }

    override fun getCount(): Int {
        return 3

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Exchange"
            2 -> "Offer"
            else-> "Gift"
        }
    }
}
