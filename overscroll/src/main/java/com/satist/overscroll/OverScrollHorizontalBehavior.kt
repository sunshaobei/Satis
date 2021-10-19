package com.satist.overscroll

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * 水平方向上的弹性滑动和惯性滑动效果
 */
class OverScrollHorizontalBehavior : BaseOverScrollBehavior {
    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onStartNestedScroll(
        parent: CoordinatorLayout, child: View,
        directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int
    ): Boolean {
        return if (child !== target) {
            false
        } else nestedScrollAxes and ViewCompat.SCROLL_AXIS_HORIZONTAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        consumed[0] = onNestedPreScrollInner(coordinatorLayout, child, target, dx, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        type: Int
    ) {
        onNestedScrollInner(coordinatorLayout, child, target, dxConsumed, dxUnconsumed, type)
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return onNestedPreFlingInner(coordinatorLayout, child, target, velocityX)
    }

    public override fun updateOffset(child: View, offset: Int) {
        child.translationX = offset.toFloat()
    }

    override fun getOffset(child: View): Int {
        return child.translationX.toInt()
    }

    override fun getMaxOffset(child: View): Int {
        return child.width
    }

    override fun getMinOffset(child: View): Int {
        return -child.width
    }
}