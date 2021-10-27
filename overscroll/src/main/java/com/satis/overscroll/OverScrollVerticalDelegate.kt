package com.satis.overscroll

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*

/**
 * 垂直方向上的弹性滑动和惯性滑动效果
 */
class OverScrollVerticalDelegate(contentView: View) : OverScrollDelegate(contentView) {

    private var mPaddingTop = Int.MAX_VALUE
    private var mPaddingBottom = Int.MAX_VALUE
    private var mOffset = 0


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

    private fun View.setTopPadding(target: View?,offset: Int){
        setPadding(paddingLeft,mPaddingTop+offset,paddingRight,paddingBottom)
        if (target is RecyclerView && offset > mOffset){
            val layoutManager = target.layoutManager
            if (layoutManager is LinearLayoutManager){
                layoutManager.scrollToPositionWithOffset(0,Int.MAX_VALUE)
            }else if (layoutManager is StaggeredGridLayoutManager){
                layoutManager.scrollToPositionWithOffset(0,Int.MAX_VALUE)
            }
        }
    }
    private fun View.setBottomPadding(target: View?,offset: Int){
        setPadding(paddingLeft,paddingTop,paddingRight,mPaddingBottom-offset)
        if ( offset < mOffset){
            if (target is RecyclerView){
                val layoutManager = target.layoutManager
                if (layoutManager is LinearLayoutManager){
//                layoutManager.stackFromEnd = true
                    layoutManager.scrollToPositionWithOffset( target.adapter!!.itemCount - 1,Int.MIN_VALUE)
                }else if (layoutManager is StaggeredGridLayoutManager){
                    layoutManager.scrollToPositionWithOffset(target.adapter!!.itemCount - 1,Int.MIN_VALUE)
                }
            }else if (target is NestedScrollView){
                target.scrollBy(0,-offset)
            }
        }
    }

    override fun updateOffset(target: View, offset: Int,topback: Int) {
        target.run {
            if (offset>0){
                if (mPaddingTop == Int.MAX_VALUE){
                    mPaddingTop = paddingTop
                }
               setTopPadding(target,offset)
            }else if (offset<0){
                if (mPaddingBottom == Int.MAX_VALUE){
                    mPaddingBottom = paddingBottom
                }
                setBottomPadding(target,offset)
//                translationY = offset.toFloat()
            }else{
                if (mOffset> 0 ){
                    setTopPadding(target,offset)
                }else{
                   setBottomPadding(target,offset)
                }
                mPaddingTop = Int.MAX_VALUE
                mPaddingBottom = Int.MAX_VALUE
            }
            mOffset = offset
        }

//        child.translationY = offset.toFloat()
    }

    override fun getOffset(child: View): Int {
        return mOffset
    }

    override fun getMaxOffset(child: View): Int {
        return child.height
    }

    override fun getMinOffset(child: View): Int {
        return -child.height
    }
}