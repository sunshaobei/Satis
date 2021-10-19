package com.satist.overscroll

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.satist.overscroll.IOverScrollCallback.ScrollDirection
import androidx.core.view.ViewCompat
import androidx.core.math.MathUtils
import kotlin.math.abs

/**
 * 垂直方向上的弹性滑动和惯性滑动效果
 */
abstract class BaseOverScrollBehavior : CoordinatorLayout.Behavior<View>, IOverScroll {
    private val mSpringBackInterpolator: Interpolator = DecelerateInterpolator(0.8f)
    private var mSpringBackAnimator: ValueAnimator? = null
    private var mOverScroller: OverScroller? = null

    @ScrollDirection
    private var mDirectionToEnd = 0

    @ScrollDirection
    private var mDirectionToStart = 0

    constructor() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    abstract override fun onStartNestedScroll(
        parent: CoordinatorLayout, child: View,
        directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int
    ): Boolean

    override fun onNestedScrollAccepted(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ) {
        if (child !== target) {
            return
        }
        if (type == ViewCompat.TYPE_TOUCH) {
            stopSpringBack(child)
        }
        if (type == ViewCompat.TYPE_TOUCH) {
            if (mOverScroller != null) {
                mOverScroller!!.forceFinished(true)
            }
        }
        if (axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0) {
            mDirectionToEnd = IOverScrollCallback.Companion.DIRECTION_DOWN
            mDirectionToStart = IOverScrollCallback.Companion.DIRECTION_UP
        } else {
            mDirectionToEnd = IOverScrollCallback.Companion.DIRECTION_RIGHT
            mDirectionToStart = IOverScrollCallback.Companion.DIRECTION_LEFT
        }
    }

    abstract override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dx: Int, dy: Int, consumed: IntArray, type: Int
    )

    /**
     * @return consumed distance
     */
    protected fun onNestedPreScrollInner(
        coordinatorLayout: CoordinatorLayout?, child: View,
        target: View, distance: Int, type: Int
    ): Int {
        if (child !== target) {
            return 0
        }
        val overscrollListener = child as IOverScrollCallback
        if (distance != 0) {
            val min: Int
            val max: Int
            if (distance < 0) { // We're scrolling to end
                if (!overscrollListener.canScroll(this, child, mDirectionToEnd)) {
                    return 0
                }
                min = getOffset(child)
                max = 0
            } else {  // We're scrolling to start
                if (!overscrollListener.canScroll(this, child, mDirectionToStart)) {
                    return 0
                }
                min = 0
                max = getOffset(child)
            }
            if (min != max) {
                return scrollWithoutDampingFactor(child, distance, min, max)
            }
        }
        return 0
    }

    abstract override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
        type: Int
    )

    protected fun onNestedScrollInner(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, distanceConsumed: Int, distanceUnconsumed: Int,
        type: Int
    ) {
        if (child !== target) {
            return
        }
        val overscrollListener = child as IOverScrollCallback
        if (distanceUnconsumed != 0) { // fix nested scroll bugs
            coordinatorLayout.requestDisallowInterceptTouchEvent(true)
        }
        if (distanceUnconsumed < 0) {
            // If the scrolling view is scrolling to end but not consuming, it's probably be at
            // the top of it's content
            if (!overscrollListener.canScroll(this, child, mDirectionToEnd)) {
                return
            }
            if (type == ViewCompat.TYPE_TOUCH) {
                scroll(child, distanceUnconsumed, 0, getMaxOffset(child))
            } else { // fling
                if (mOverScroller == null || !mOverScroller!!.computeScrollOffset()
                    || Math.abs(mOverScroller!!.currVelocity) < Math.abs(
                        overscrollListener.getMinFlingVelocity(
                            this,
                            child,
                            mDirectionToEnd
                        )
                    ) // too slow
                    || getOffset(child) >= overscrollListener.getMaxFlingOffset(
                        this,
                        child,
                        mDirectionToEnd
                    )
                ) { // reach edge
                    ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH)
                } else {
                    scroll(
                        child,
                        distanceUnconsumed,
                        getOffset(child),
                        overscrollListener.getMaxFlingOffset(this, child, mDirectionToEnd)
                    )
                }
            }
        } else if (distanceUnconsumed > 0) {
            if (!overscrollListener.canScroll(this, child, mDirectionToStart)) {
                return
            }
            if (type == ViewCompat.TYPE_TOUCH) {
                scroll(child, distanceUnconsumed, getMinOffset(child), 0)
            } else { // fling
                if (mOverScroller == null || !mOverScroller!!.computeScrollOffset()
                    || abs(mOverScroller!!.currVelocity) < abs(
                        overscrollListener.getMinFlingVelocity(
                            this,
                            child,
                            mDirectionToStart
                        )
                    ) // too slow
                    || getOffset(child) <= overscrollListener.getMaxFlingOffset(
                        this,
                        child,
                        mDirectionToStart
                    )
                ) { // reach edge
                    ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH)
                } else {
                    scroll(
                        child,
                        distanceUnconsumed,  // slow down
                        overscrollListener.getMaxFlingOffset(this, child, mDirectionToStart),
                        getOffset(child)
                    )
                }
            }
        }
    }

    abstract override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean

    protected fun onNestedPreFlingInner(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocity: Float
    ): Boolean {
        if (child !== target) {
            return false
        }
        if (mOverScroller == null) {
            mOverScroller = OverScroller(coordinatorLayout.context)
        }
        /* velocityX = 0, velocityY = velocity
                    or
           velocityX = velocity, velocityY = 0
         */mOverScroller!!.fling(0, 0, 0, velocity.toInt(), 0, 0, Int.MIN_VALUE, Int.MAX_VALUE)
        return false
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout, child: View,
        target: View, type: Int
    ) {
        if (child !== target) {
            return
        }
        if (type == ViewCompat.TYPE_TOUCH) { // touching
            if (getOffset(child) != 0) { // and out of bound
                if (mOverScroller == null || !mOverScroller!!.computeScrollOffset()) { // no fling
                    springBack(child)
                }
            }
        } else {
            if (getOffset(child) != 0) {
                springBack(child)
            }
        }
    }

    /**
     *
     */
    private fun computerWithDampingFactor(
        child: View,
        distance: Int
    ): Int {
        val overscroll = child as IOverScrollCallback
        val direction = if (distance > 0) mDirectionToStart else mDirectionToEnd
        var factor = overscroll.getDampingFactor(this, child, direction)
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
    private fun computerOffset(child: View, newOffset: Int, minOffset: Int, maxOffset: Int): Int {
        var newOffset = newOffset
        val curOffset = getOffset(child)
        var consumed = 0
        if (curOffset >= minOffset && curOffset <= maxOffset) {
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

    override fun stopSpringBack(child: View) {
        if (mSpringBackAnimator != null) {
            if (mSpringBackAnimator!!.isRunning) {
                mSpringBackAnimator!!.cancel()
            }
        }
        val overScrollCallback = child as IOverScrollCallback
        overScrollCallback.onStopSpringingBack(this, child)
    }

    override fun springBack(child: View) {
        val overScroll = child as IOverScrollCallback
        val startOffset = getOffset(child)
        if (startOffset == 0) {
            return
        }
        if (overScroll.onSpringBack(this, child)) {
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
            Math.abs(startOffset) * 1f / getMaxOffset(child) * MAX_BOUNCE_BACK_DURATION_MS
        mSpringBackAnimator!!.duration =
            Math.max(bounceBackDuration.toInt(), MIN_BOUNCE_BACK_DURATION_MS).toLong()
        mSpringBackAnimator!!.interpolator = mSpringBackInterpolator
        mSpringBackAnimator!!.setIntValues(startOffset, 0)
        mSpringBackAnimator!!.start()
    }

    override fun setOffset(child: View, offset: Int) {
        val overscrollListener = child as IOverScrollCallback
        updateOffset(child, offset)
        overscrollListener.onOffsetChanged(this, child, getOffset(child))
    }

    protected abstract fun updateOffset(child: View, offset: Int)

    companion object {
        private const val MAX_BOUNCE_BACK_DURATION_MS = 300
        private const val MIN_BOUNCE_BACK_DURATION_MS = 150
    }
}