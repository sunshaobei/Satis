package com.satis.overscroll.api

import android.view.View
import com.satis.overscroll.RefreshState

abstract class RefreshImp(val mRefreshView:View) {
    private var mRefreshState = RELEASE
    fun setRefreshState(@RefreshState state:Int){
        this.mRefreshState = state
        onStateChange(mRefreshState)
    }
    abstract fun onStateChange(@RefreshState state:Int)
    fun getRefreshState():Int{
        return mRefreshState
    }
    abstract fun updateOffset(offset:Int)
    abstract fun getRefreshHeight():Int
    companion object {
        const val PULL_DOWN = 1 //下拉
        const val PULL_DOWN_OVER = 2 //下拉超出刷新高度
        const val REFRESHING = 3 // 刷新
        const val RELEASE = 4 //释放
    }
}