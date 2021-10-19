package com.satist.overscroll

import android.view.View

interface IOverScroll {
    fun setOffset(child: View, offset: Int)
    fun getOffset(child: View): Int
    fun getMaxOffset(child: View): Int
    fun getMinOffset(child: View): Int
    fun stopSpringBack(child: View)
    fun springBack(child: View)
}