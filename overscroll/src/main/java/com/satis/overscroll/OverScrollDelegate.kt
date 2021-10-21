package com.satis.overscroll

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.OverScroller
import com.satis.overscroll.api.OverScrollImp
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.core.math.MathUtils
import com.satis.overscroll.api.OverScrollOffsetChangeListener
import java.util.ArrayList
import kotlin.math.abs

abstract class OverScrollDelegate(private val mContentView: View) : OverScrollImp {
    private val mSpringBackInterpolator: Interpolator = DecelerateInterpolator(0.8f)
    private var mSpringBackAnimator: ValueAnimator? = null
    private var mOverScroller: OverScroller? = null

    @ScrollDirection
    private var mDirectionToEnd = 0

    @ScrollDirection
    private var mDirectionToStart = 0
    abstract fun onStartNestedScroll(nestedScrollAxes: Int, type: Int): Boolean
    fun onNestedScrollAccepted(target: View, axes: Int, type: Int) {
        if (type == ViewCompat.TYPE_TOUCH) {
            stopSpringBack(target)
        }
        if (type == ViewCompat.TYPE_TOUCH) {
            if (mOverScroller != null) {
                mOverScroller!!.forceFinished(true)
            }
        }
        if (axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0) {
            mDirectionToEnd = OverScrollImp.DIRECTION_DOWN
            mDirectionToStart = OverScrollImp.DIRECTION_UP
        } else {
            mDirectionToEnd = OverScrollImp.DIRECTION_RIGHT
            mDirectionToStart = OverScrollImp.DIRECTION_LEFT
        }
    }

    abstract fun onNestedPreScroll(
        target: View?, dx: Int, dy: Int, consumed: IntArray, type: Int
    )

    abstract fun onNestedScroll(
        target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        type: Int
    )

    /**
     * @return consumed distance
     */
    protected fun onNestedPreScrollInner(target: View, distance: Int, type: Int): Int {
        if (distance != 0) {
            val min: Int
            val max: Int
            if (distance < 0) { // We're scrolling to end
                if (!canScroll(this, target, mDirectionToEnd)) {
                    return 0
                }
                min = getOffset(target)
                max = 0
            } else {  // We're scrolling to start
                if (!canScroll(this, target, mDirectionToStart)) {
                    return 0
                }
                min = 0
                max = getOffset(target)
            }
            if (min != max) {
                return scrollWithoutDampingFactor(target, distance, min, max)
            }
        }
        return 0
    }

    protected fun onNestedScrollInner(
        target: View, distanceUnconsumed: Int, type: Int
    ) {
        if (distanceUnconsumed != 0) { // fix nested scroll bugs
            mContentView.parent.requestDisallowInterceptTouchEvent(true)
        }
        if (distanceUnconsumed < 0) {
            // If the scrolling view is scrolling to end but not consuming, it's probably be at
            // the top of it's content
            if (!canScroll(this, target, mDirectionToEnd)) {
                return
            }
            if (type == ViewCompat.TYPE_TOUCH) {
                scroll(target, distanceUnconsumed, 0, getMaxOffset(target))
            } else { // fling
                if (mOverScroller == null || !mOverScroller!!.computeScrollOffset()
                    || Math.abs(mOverScroller!!.currVelocity) < Math.abs(
                        getMinFlingVelocity(
                            this,
                            target,
                            mDirectionToEnd
                        )
                    ) // too slow
                    || getOffset(target) >= getMaxFlingOffset(this, target, mDirectionToEnd)
                ) { // reach edge
                    ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH)
                } else {
                    scroll(
                        target, distanceUnconsumed,
                        getOffset(target), getMaxFlingOffset(this, target, mDirectionToEnd)
                    )
                }
            }
        } else if (distanceUnconsumed > 0) {
            if (!canScroll(this, target, mDirectionToStart)) {
                return
            }
            if (type == ViewCompat.TYPE_TOUCH) {
                scroll(target, distanceUnconsumed, getMinOffset(target), 0)
            } else { // fling
                if (mOverScroller == null || !mOverScroller!!.computeScrollOffset()
                    || abs(mOverScroller!!.currVelocity) < Math.abs(
                        getMinFlingVelocity(
                            this,
                            target,
                            mDirectionToStart
                        )
                    ) // too slow
                    || getOffset(target) <= getMaxFlingOffset(this, target, mDirectionToStart)
                ) { // reach edge
                    ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH)
                } else {
                    scroll(
                        target, distanceUnconsumed,  // slow down
                        getMaxFlingOffset(this, target, mDirectionToStart), getOffset(target)
                    )
                }
            }
        }
    }

    abstract fun onNestedPreFling(target: View?, velocityX: Float, velocityY: Float): Boolean
    protected fun onNestedPreFlingInner(target: View, velocity: Float): Boolean {
        if (mOverScroller == null) {
            mOverScroller = OverScroller(target.context)
        }
        mOverScroller!!.fling(0, 0, 0, velocity.toInt(), 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
        return false
    }

    fun onStopNestedScroll(target: View, type: Int) {
        if (type == ViewCompat.TYPE_TOUCH) { // touching
            if (getOffset(target) != 0) { // and out of bound
                if (mOverScroller == null || !mOverScroller!!.computeScrollOffset()) { // no fling
                    springBack(target)
                }
            }
        } else {
            if (getOffset(target) != 0) {
                springBack(target)
            }
        }
    }

    /**
     *
     */
    private fun computerWithDampingFactor(child: View, distance: Int): Int {
        val direction = if (distance > 0) mDirectionToStart else mDirectionToEnd
        var factor = getDampingFactor(this, child, direction)
        if (factor == 0f) {
            factor = 1f
        }
        return (distance / factor + 0.5f).toInt()
    }

    private fun scrollWithoutDampingFactor(
        child: View,
        distance: Int,
        minOffset: Int,
        maxOffset: Int
    ): Int {
        return computerOffset(child, getOffset(child) - distance, minOffset, maxOffset)
    }

    private fun scroll(child: View, distance: Int, minOffset: Int, maxOffset: Int): Int {
        return computerOffset(
            child,
            getOffset(child) - computerWithDampingFactor(child, distance),
            minOffset,
            maxOffset
        )
    }

    /**
     * @return 消耗掉距离
     */
    private fun computerOffset(child: View, offset: Int, minOffset: Int, maxOffset: Int): Int {
        var newOffset = offset
        val curOffset = getOffset(child)
        var consumed = 0
        if (curOffset in minOffset..maxOffset) {
            // If we have some scrolling range, and we're currently within the min and max
            // offsets, calculate a new offset
            newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset)
            if (curOffset != newOffset) {
                setOffset(child, newOffset)
                // Update how much dy we have consumed
                consumed = curOffset - newOffset
            }
        }
        return consumed
    }

    override fun stopSpringBack(child: View?) {
        if (mSpringBackAnimator != null) {
            if (mSpringBackAnimator!!.isRunning) {
                mSpringBackAnimator!!.cancel()
            }
        }
        onStopSpringingBack(this, child)
    }

    override fun springBack(child: View) {
        val startOffset = getOffset(child)
        if (startOffset == 0) {
            return
        }
        if (onSpringBack(this, child)) {
            return
        }
        if (mSpringBackAnimator == null) {
            mSpringBackAnimator = ValueAnimator.ofInt()
            mSpringBackAnimator?.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                setOffset(child, value)
            }
        }
        if (mSpringBackAnimator!!.isStarted) {
            return
        }
        val bounceBackDuration =
            abs(startOffset) * 1f / getMaxOffset(child) * MAX_BOUNCE_BACK_DURATION_MS
        mSpringBackAnimator!!.duration =
            bounceBackDuration.toInt().coerceAtLeast(MIN_BOUNCE_BACK_DURATION_MS).toLong()
        mSpringBackAnimator!!.interpolator = mSpringBackInterpolator
        mSpringBackAnimator!!.setIntValues(startOffset, 0)
        mSpringBackAnimator!!.start()
    }

    override fun setOffset(child: View, offset: Int) {
        updateOffset(child, offset)
        onOffsetChanged(this, child, getOffset(child))
    }

    open fun updateOffset(child: View, offset: Int) {
        child.translationY = offset.toFloat()
    }

    override fun getOffset(child: View): Int {
        return child.translationY.toInt()
    }

    override fun getMaxOffset(child: View): Int {
        return child.height
    }

    override fun getMinOffset(child: View): Int {
        return -child.height
    }

    private var mMinFlingVelocity = 0
    override fun canScroll(
        overScroll: OverScrollImp?,
        child: View?,
        @ScrollDirection scrollDirection: Int
    ): Boolean {
        return true
    }

    override fun getMaxFlingOffset(
        overScroll: OverScrollImp?,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Int {
        return if (scrollDirection == OverScrollImp.DIRECTION_DOWN || scrollDirection == OverScrollImp.Companion.DIRECTION_RIGHT) {
            child.height / 3
        } else {
            -child.height / 3
        }
    }

    override fun getDampingFactor(
        overScroll: OverScrollImp,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Float {
        val absOffset = abs(overScroll.getOffset(child))
        val progress = absOffset * 1f / child.height
        return 1 + 4 * progress // factor = {1, 5}
    }

    override fun getMinFlingVelocity(
        overScroll: OverScrollImp?,
        child: View,
        scrollDirection: Int
    ): Int {
        if (mMinFlingVelocity <= 0) {
            mMinFlingVelocity = ViewConfiguration.get(child.context).scaledMinimumFlingVelocity * 15
        }
        return mMinFlingVelocity
    }

    override fun onOffsetChanged(overScroll: OverScrollImp?, child: View?, offset: Int) {
        val tag = mContentView.getTag(R.id.tag_overscroll_offset_listener)
        tag?.let {
            (tag as OverScrollOffsetChangeListener).onOffsetChanged(mContentView,offset)
        }
    }

    override fun onSpringBack(overScroll: OverScrollImp?, child: View?): Boolean {
        return false
    }

    override fun onStopSpringingBack(overScroll: OverScrollImp?, child: View?) {}

    companion object {
        private const val MAX_BOUNCE_BACK_DURATION_MS = 300
        private const val MIN_BOUNCE_BACK_DURATION_MS = 150
        @JvmStatic
        fun createByTarget(target: View): OverScrollDelegate {
            if (target is RecyclerView) {
                val layoutManager = target.layoutManager
                var orientation = RecyclerView.VERTICAL
                if (layoutManager is LinearLayoutManager) {
                    orientation = layoutManager.orientation
                } else if (layoutManager is StaggeredGridLayoutManager) {
                    orientation = layoutManager.orientation
                }
                if (orientation == RecyclerView.HORIZONTAL) {
                    return OverScrollHorizontalDelegate(target)
                }
            }
            return OverScrollVerticalDelegate(target)
        }
    }
}