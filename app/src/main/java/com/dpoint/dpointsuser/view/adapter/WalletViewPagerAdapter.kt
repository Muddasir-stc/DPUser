package com.dpoint.dpointsuser.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dpoint.dpointsuser.view.module.dashboard.MyGiftCardFragment
import com.dpoint.dpointsuser.view.module.membership.MemberShipCardActivity
import com.dpoints.view.module.dashboard.OfferFragment

class WalletViewPagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return MemberShipCardActivity()
        } else if (position == 1) {
            return MyGiftCardFragment()
        } else {
            return OfferFragment()
        }
    }

    override fun getCount(): Int {
        return 2

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "My Membership Cards"
            1 -> "My Gifts Cards"
            else -> "Offers"
        }
    }
}
