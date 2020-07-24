package com.dpoint.dpointsuser.utilities

import androidx.viewpager.widget.ViewPager


inline fun ViewPager.onPageSelected(crossinline block: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {}

        override fun onPageSelected(position: Int) {
            block(position)
        }
    })
}
