package com.satis.overscroll

import android.view.View
import androidx.core.view.ViewCompat

/**
 * 垂直方向上的弹性滑动和惯性滑动效果
 */
class OverScrollVerticalDelegate(contentView: View) : OverScrollDelegate(contentView) {
    override fun onStartNestedScroll(nestedScrollAxes: Int, type: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        consumed[1] = onNestedPreScrollInner(target!!, dy, type)
    }

    override fun onNestedScroll(
        target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        type: Int
    ) {
        onNestedScrollInner(target!!, dyUnconsumed, type)
    }

    override fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean {
        return onNestedPreFlingInner(target!!, velocityY)
    }

    override fun updateOffset(child: View, offset: Int) {
        child.translationY = offset.toFloat();
    }

    override fun getOffset(child: View): Int {
        return child.translationY.toInt()
    }

    override fun getMaxOffset(child: View): Int {
        return child.height
    }

    override fun getMinOffset(child: View): Int {
        return -child.height
    }
}