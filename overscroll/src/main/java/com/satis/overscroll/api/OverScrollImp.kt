package com.satis.overscroll.api

import android.view.View
import com.satis.overscroll.ScrollDirection

interface OverScrollImp {
    fun setOffset(child: View, offset: Int)
    fun getOffset(child: View): Int
    fun getMaxOffset(child: View): Int
    fun getMinOffset(child: View): Int
    fun stopSpringBack(child: View?)
    fun springBack(child: View)

    /**
     * @param overScroll
     * @param child           the child view of the CoordinatorLayout this Behavior is associated with. 跟当前behavior绑定的CoordinatorLayout的子view
     * @param scrollDirection [.DIRECTION_UP] or [.DIRECTION_DOWN]. 过度滑动的方向
     * @return true if the child view can scroll in the scroll direction. 返回true表示子view可以在相应的方向上过度滑动
     */
    fun canScroll(
        overScroll: OverScrollImp?,
        child: View?,
        @ScrollDirection scrollDirection: Int
    ): Boolean

    /**
     * 最大的惯性滑动的偏移值
     *
     * @param overScroll
     * @param child
     * @param scrollDirection
     * @return max offsets when fling, in px
     */
    fun getMaxFlingOffset(
        overScroll: OverScrollImp?,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Int

    /**
     * Damping factor, the larger the value, the harder it is to scroll
     * 阻尼因子,值越大则摩擦越大越难滑动
     *
     * @param overScroll
     * @param child
     * @param scrollDirection
     * @return Damping factor when scrolling, should be positive. Only take effect when you offset the child view away.
     */
    fun getDampingFactor(
        overScroll: OverScrollImp,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Float

    /**
     * 产生惯性滑动的最小速度(取绝对值)，小于该速度时会停止惯性滑动.
     *
     * @param overScroll
     * @param child
     * @param scrollDirection
     * @return Minimum velocity (the absolute value) to occur a fling,  in pixels per second. If the velocity is less than the min, the child view will stop the fling
     */
    fun getMinFlingVelocity(
        overScroll: OverScrollImp?,
        child: View,
        @ScrollDirection scrollDirection: Int
    ): Int

    /**
     * callback when the child view's offset changed．
     * 子view发生偏移时回调
     *
     * @param overScroll
     * @param child
     * @param offset
     */
    fun onOffsetChanged(overScroll: OverScrollImp?, child: View?, offset: Int)

    /**
     * callback before springing back
     * @param overScroll
     * @param child
     * @return true if you have consumed the event, false if you haven't. The default implementation always returns false.
     */
    fun onSpringBack(overScroll: OverScrollImp?, child: View?): Boolean

    /**
     * callback before stop springing back
     * @param overScroll
     * @param child
     */
    fun onStopSpringingBack(overScroll: OverScrollImp?, child: View?)

    companion object {
        const val DIRECTION_UP = 1 shl 0 // Direction to start
        const val DIRECTION_DOWN = 1 shl 1 // Direction to end
        const val DIRECTION_LEFT = 1 shl 2 // Direction to start
        const val DIRECTION_RIGHT = 1 shl 3 // Direction to end
    }
}