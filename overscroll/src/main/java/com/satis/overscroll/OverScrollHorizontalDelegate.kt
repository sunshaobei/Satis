package com.satis.overscroll

import android.view.View
import androidx.core.view.ViewCompat

/**
 * 水平方向上的弹性滑动和惯性滑动效果
 */
class OverScrollHorizontalDelegate(contentView: View) : OverScrollDelegate(contentView) {
    override fun onStartNestedScroll(nestedScrollAxes: Int, type: Int): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_HORIZONTAL != 0
    }

    override fun onNestedPreScroll(
        target: View?, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        consumed[0] = onNestedPreScrollInner(target!!, dx, type)
    }

    override fun onNestedScroll(
        target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        type: Int
    ) :Int{
       return onNestedScrollInner(target!!, dxUnconsumed, type)
    }

    override fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean {
        return onNestedPreFlingInner(target!!, velocityX)
    }

    override fun updateOffset(target: View, offset: Int,@ScrollDirection scrollDirection: Int) {
        target.translationX = offset.toFloat()
    }

    override fun getOffset(target: View): Int {
        return target.translationX.toInt()
    }

    override fun getMaxOffset(target: View): Int {
        return target.width
    }

    override fun getMinOffset(target: View): Int {
        return -target.width
    }
}