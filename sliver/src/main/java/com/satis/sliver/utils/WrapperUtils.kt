package com.satis.sliver.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.satis.sliver.SpanSizeCallback

/**
 * Created by sunsh on 18/5/30.
 */
object WrapperUtils {
    @JvmStatic
    fun onAttachedToRecyclerView(
        recyclerView: RecyclerView,
        callback: SpanSizeCallback
    ) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return callback.invoke(layoutManager, spanSizeLookup, position)
                }
            }
            layoutManager.spanCount = layoutManager.spanCount
        }
    }

    @JvmStatic
    fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null
            && lp is StaggeredGridLayoutManager.LayoutParams
        ) {
            lp.isFullSpan = true
        }
    }
}