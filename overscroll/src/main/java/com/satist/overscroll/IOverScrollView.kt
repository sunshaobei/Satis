package com.satist.overscroll

interface IOverScrollView {
    var overScrollCallback: IOverScrollCallback?
    fun addOffsetChangeListener(listener: IOffsetChangeListener?)
    fun removeOffsetChangeListener(listener: IOffsetChangeListener?)
    val overScrollOffset: Int
}