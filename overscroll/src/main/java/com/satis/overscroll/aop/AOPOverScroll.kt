package com.satis.overscroll.aop

import android.view.View
import com.satis.overscroll.OverScrollDelegate.Companion.createByTarget
import com.satis.overscroll.OverScrollDelegate
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.NestedScrollView
import com.satis.overscroll.R
import com.satis.overscroll.api.RefreshImp
import kotlin.Throws
import org.aspectj.lang.ProceedingJoinPoint

object AOPOverScroll {
    private fun getOverScrollDelegate(target: Any): OverScrollDelegate? {
        if (target is RecyclerView || target is NestedScrollView) {
            var tag = (target as View).getTag(R.id.tag_overscroll_delegate)
            if (tag == null) {
                tag = createByTarget(target)
                target.setTag(R.id.tag_overscroll_delegate, tag)
            }
            return tag as OverScrollDelegate
        }
        return null
    }

    @Throws(Throwable::class)
    fun startNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target as View
        val overScrollDelegate = getOverScrollDelegate(target)
        overScrollDelegate?.run {
            if (overScrollDelegate.mRefreshImp?.getRefreshState() == RefreshImp.REFRESHING){
                return joinPoint.proceed()
            }
            if (getOffset(target) != 0) {
                if (startNestedScroll(args, target)) {
                    return true
                }
            } else {
                val b = joinPoint.proceed() as Boolean
                if (b) {
                    return true
                }
            }
        }
        return overScrollDelegate?.startNestedScroll(args, target)
    }

    private fun OverScrollDelegate.startNestedScroll(args: Array<Any>, target: View): Boolean {
        if (onStartNestedScroll(args[0] as Int, args[1] as Int)) {
            onNestedScrollAccepted(
                target,
                args[0] as Int,
                args[1] as Int
            )
            return true
        }
        return false
    }

    @Throws(Throwable::class)
    fun dispatchNestedPreScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target as View
        if (getOverScrollDelegate(target)?.mRefreshImp?.getRefreshState() == RefreshImp.REFRESHING){
            return joinPoint.proceed()
        }
        return ((getOverScrollDelegate(target)?.getOffset(target) == 0 && (joinPoint.proceed()) as Boolean)
                || dispatchNestedPreScroll(
            args,
            target
        ))
    }

    private fun dispatchNestedPreScroll(
        args: Array<Any>,
        target: View,
    ): Boolean {
        val dx = args[0] as Int
        val dy = args[1] as Int
        val consumed = args[2] as IntArray
        val scrollType = args[4] as Int
        getOverScrollDelegate(target)?.onNestedPreScroll(
            target,
           dx,
           dy,
            consumed,
            scrollType
        )
        if (consumed[1] != 0 || consumed[0] != 0) {
            return true
        }
        return false
    }

    @Throws(Throwable::class)
    fun dispatchNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target as View
        val proceed = joinPoint.proceed()
        val consumed = args[6] as IntArray
        val i = consumed[1]  //y
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate?.mRefreshImp?.getRefreshState() == RefreshImp.REFRESHING){
            return joinPoint.proceed()
        }
        if (i == 0 || overScrollDelegate?.getOffset(target) != 0) {
            val dxConsumed = args[0] as Int
            val dyConsumed = args[1] as Int
            val dxUnconsumed = args[2] as Int
            val dyUnconsumed = args[3] as Int
            val scrollType = args[5] as Int
            overScrollDelegate?.onNestedScroll(
                target ,
                dxConsumed,
                dyConsumed,
                dxUnconsumed,
                dyUnconsumed,
                scrollType
            )
        }
        return proceed
    }

    @Throws(Throwable::class)
    fun dispatchNestedPreFling(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        val velocityX = args[0] as Float
        val velocityY = args[1] as Float
        if (overScrollDelegate?.mRefreshImp?.getRefreshState() == RefreshImp.REFRESHING){
            return joinPoint.proceed()
        }
        overScrollDelegate?.onNestedPreFling(
            target as View,
            velocityX,
            velocityY
        )
        return joinPoint.proceed()
    }

    @Throws(Throwable::class)
    fun stopNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val target = joinPoint.target as View
        val args = joinPoint.args
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate?.mRefreshImp?.getRefreshState() == RefreshImp.REFRESHING){
            return joinPoint.proceed()
        }
        if (overScrollDelegate != null && args.isNotEmpty()) {
            val scrollType = args[0] as Int
            overScrollDelegate.onStopNestedScroll(target, scrollType)
        }
        return joinPoint.proceed()
    }
}