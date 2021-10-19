package com.satist.overscroll.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.satist.overscroll.IOverScrollCallback
import com.satist.overscroll.IOverScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout.AttachedBehavior
import com.satist.overscroll.IOffsetChangeListener
import com.satist.overscroll.SimpleOverScrollCallback
import com.satist.overscroll.IOverScroll
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.satist.overscroll.OverScrollVerticalBehavior
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 * over scrolling horizontally for RecyclerView
 *
 * @author ziwei huang
 */
open class OverScrollVerticalRecyclerView : RecyclerView, IOverScrollCallback, IOverScrollView,
    AttachedBehavior {
    private var mOffsetChangeListeners: MutableList<IOffsetChangeListener?>? = null
    val defaultOverCallback: IOverScrollCallback = SimpleOverScrollCallback()
    override var overScrollCallback: IOverScrollCallback? = defaultOverCallback
    private var mOverScrollOffset: Int? = null

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun canScroll(overScroll: IOverScroll?, child: View, scrollDirection: Int): Boolean {
        return if (overScrollCallback != null) {
            overScrollCallback!!.canScroll(overScroll, child, scrollDirection)
        } else false
    }

    override fun getMaxFlingOffset(
        overScroll: IOverScroll?,
        child: View,
        scrollDirection: Int
    ): Int {
        return if (overScrollCallback != null) {
            overScrollCallback!!.getMaxFlingOffset(overScroll, child, scrollDirection)
        } else 0
    }

    override fun getDampingFactor(
        overScroll: IOverScroll,
        child: View,
        scrollDirection: Int
    ): Float {
        return if (overScrollCallback != null) {
            overScrollCallback!!.getDampingFactor(overScroll, child, scrollDirection)
        } else 0F
    }

    override fun getMinFlingVelocity(
        overScroll: IOverScroll?,
        child: View,
        scrollDirection: Int
    ): Int {
        return if (overScrollCallback != null) {
            overScrollCallback!!.getMinFlingVelocity(overScroll, child, scrollDirection)
        } else 0
    }

    override fun onOffsetChanged(overScroll: IOverScroll?, child: View, offset: Int) {
        mOverScrollOffset = offset
        if (overScrollCallback != null) {
            overScrollCallback!!.onOffsetChanged(overScroll, child, offset)
        }
        for (i in mOffsetChangeListeners!!.indices) {
            mOffsetChangeListeners!![i]!!.onOffsetChanged(child!!, offset)
        }
    }

    override fun onSpringBack(overScroll: IOverScroll?, child: View): Boolean {
        return if (overScrollCallback != null) {
            overScrollCallback!!.onSpringBack(overScroll, child)
        } else false
    }

    override fun onStopSpringingBack(overScroll: IOverScroll?, child: View) {
        if (overScrollCallback != null) {
            overScrollCallback!!.onStopSpringingBack(overScroll, child)
        }
    }

    override fun addOffsetChangeListener(listener: IOffsetChangeListener?) {
        if (mOffsetChangeListeners == null) {
            mOffsetChangeListeners = ArrayList()
        }
        if (listener != null && !mOffsetChangeListeners!!.contains(listener)) {
            mOffsetChangeListeners!!.add(listener)
        }
    }

    override fun removeOffsetChangeListener(listener: IOffsetChangeListener?) {
        mOffsetChangeListeners!!.remove(listener)
    }

    override val overScrollOffset: Int
        get() = mOverScrollOffset!!

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return OverScrollVerticalBehavior()
    }
}