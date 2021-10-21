package com.satis.overscroll.api

import android.view.View

interface OverScrollOffsetChangeListener {
    /**
     * overscroll 越界 距离监听
     * @param child
     * @param offset
     */
    fun onOffsetChanged(child: View?, offset: Int)
}