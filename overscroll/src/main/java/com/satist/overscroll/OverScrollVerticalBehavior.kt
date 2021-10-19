package com.satist.overscroll

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * 垂直方向上的弹性滑动和惯性滑动效果
 */
class OverScrollVerticalBehavior : BaseOverScrollBehavior {
    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onStartNestedScroll(
        parent: CoordinatorLayout, child: View,
        directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int
    ): Boolean {
        return if (child !== target) {
            false
        } else nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    ) {
        consumed[1] = onNestedPreScrollInner(coordinatorLayout, child, target, dy, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        type: Int
    ) {
        onNestedScrollInner(coordinatorLayout, child, target, dyConsumed, dyUnconsumed, type)
    }

    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return onNestedPreFlingInner(coordinatorLayout, child, target, velocityY)
    }

    public override fun updateOffset(child: View, offset: Int) {
//        mOffset = offset
//        if (mPaddingTop == mPaddingBottom){
//            if (offset>0){
//                mOriPaddingTop = child.paddingTop
//                mPaddingTop = offset
//                child.setPadding(0,mPaddingTop+mOriPaddingTop,0,0)
//            }else{
//                mOriPaddingBottom= child.paddingBottom
//                mPaddingBottom = offset
//                child.setPadding(0,0,0,mPaddingBottom+mOriPaddingBottom)
//            }
//        }else{
//            if (mPaddingTop>0){
//                mPaddingTop = offset
//                child.setPadding(0,mPaddingTop+mOriPaddingTop,0,0)
//            }else{
//                mPaddingBottom = offset
//                child.setPadding(0,0,0,mPaddingBottom+mOriPaddingBottom)
//            }
//        }
        child.translationY = offset.toFloat()
    }

//    private var mOriPaddingTop = 0
//    private var mOriPaddingBottom = 0
//    private var mPaddingTop = 0
//    private var mPaddingBottom = 0
//    private var mOffset = 0

    override fun getOffset(child: View): Int {
//        return mOffset
        return child.translationY.toInt()
    }

    override fun getMaxOffset(child: View): Int {
        return child.height
    }

    override fun getMinOffset(child: View): Int {
        return -child.height
    }
}