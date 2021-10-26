package com.satis.overscroll

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
    ):Int {
        return onNestedScrollInner(target!!, dyUnconsumed, type)
    }

    override fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean {
        return onNestedPreFlingInner(target!!, velocityY)
    }

    override fun updateOffset(child: View, offset: Int) {
        child.translationY = offset.toFloat()

//        if (offset>0){
//            child.setPadding(0,offset,0,0)
//        }else{
//            child.setPadding(0,0,0,offset)
//        }


//        val recyclerView = child as RecyclerView
//        val childAt = recyclerView.getChildAt(0)
//        val childAdapterPosition = recyclerView.getChildAdapterPosition(childAt)
//        if (childAdapterPosition == 0){
//            val layoutParams = childAt.layoutParams
//            layoutParams.height = offset
//            childAt.requestLayout()
//        }
    }

    override fun getOffset(child: View): Int {
//        val recyclerView = child as RecyclerView
//        val childAt = recyclerView.getChildAt(0)
//        val childAdapterPosition = recyclerView.getChildAdapterPosition(childAt)
//        if (childAdapterPosition == 0){
//            val layoutParams = childAt.layoutParams
//            return layoutParams.height
//        }
//        return 0
        return child.translationY.toInt()

//        return if (child.paddingTop>0){
//            child.paddingTop
//        }else{
//            child.paddingBottom
//        }
    }

    override fun getMaxOffset(child: View): Int {
        return child.height
    }

    override fun getMinOffset(child: View): Int {
        return -child.height
    }
}