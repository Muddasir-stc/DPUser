package com.dpoints.dpointsmerchant.utilities

import android.graphics.Rect
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(offset: Int) : RecyclerView.ItemDecoration() {
    var offset: Int = 0
    init {
        this.offset = offset
    }

    override fun getItemOffsets(
        @NonNull outRect: Rect,
        @NonNull view: View,
        @NonNull parent: RecyclerView,
        @NonNull state: RecyclerView.State
    ) {
        outRect.right = offset
        outRect.bottom = offset

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = offset
        }

    }
}