package com.satist.overscroll

import android.view.View

interface IOffsetChangeListener {
    /**
     * Called when the child's layout offset has been changed. This allows
     * child views to implement custom behavior based on the offset (for instance pinning a
     * view at a certain y value).
     *
     * @param child the child view which offset has changed
     * @param offset the vertical offset for the child, in px
     */
    fun onOffsetChanged(child: View, offset: Int)
}