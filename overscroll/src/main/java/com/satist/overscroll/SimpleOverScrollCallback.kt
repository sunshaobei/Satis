package com.satist.overscroll

import android.view.View
import android.view.ViewConfiguration
import com.satist.overscroll.IOverScrollCallback.ScrollDirection

class SimpleOverScrollCallback : IOverScrollCallback {
    private var mMinFlingVelocity = 0
    override fun canScroll(
        overScroll: IOverScroll?,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Boolean {
        return true
    }

    override fun getMaxFlingOffset(
        overScroll: IOverScroll?,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Int {
        return if (scrollDirection == IOverScrollCallback.Companion.DIRECTION_DOWN || scrollDirection == IOverScrollCallback.Companion.DIRECTION_RIGHT) {
            child.height / 3
        } else {
            -child.height / 3
        }
    }

    override fun getDampingFactor(
        overScroll: IOverScroll,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Float {
        val absOffset = Math.abs(overScroll.getOffset(child))
        val progress = absOffset * 1f / child.height
        return 1 + 4 * progress // factor = {1, 5}
    }

    override fun getMinFlingVelocity(
        overScroll: IOverScroll?,
        child: View,
        scrollDirection: Int
    ): Int {
        if (mMinFlingVelocity <= 0) {
            mMinFlingVelocity = ViewConfiguration.get(child.context).scaledMinimumFlingVelocity * 15
        }
        return mMinFlingVelocity
    }

    override fun onOffsetChanged(overScroll: IOverScroll?, child: View, offset: Int) {}
    override fun onSpringBack(overScroll: IOverScroll?, child: View): Boolean {
        return false
    }

    override fun onStopSpringingBack(overScroll: IOverScroll?, child: View) {}
}