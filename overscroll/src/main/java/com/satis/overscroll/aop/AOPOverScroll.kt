package com.satis.overscroll.aop

import android.view.View
import com.satis.overscroll.OverScrollDelegate.Companion.createByTarget
import com.satis.overscroll.OverScrollDelegate
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.NestedScrollView
import com.satis.overscroll.R
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
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate != null) {
            if (overScrollDelegate.onStartNestedScroll(args[0] as Int, args[1] as Int)) {
                overScrollDelegate.onNestedScrollAccepted(
                    (target as View),
                    args[0] as Int,
                    args[1] as Int
                )
                return true
            }
        }
        return joinPoint.proceed()
    }

    @Throws(Throwable::class)
    fun dispatchNestedPreScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate != null) {
            overScrollDelegate.onNestedPreScroll(
                target as View,
                args[0] as Int,
                args[1] as Int,
                (args[2] as IntArray),
                args[4] as Int
            )
            if ((args[2] as IntArray)[1] != 0) {
                return true
            }
        }
        return joinPoint.proceed()
    }

    @Throws(Throwable::class)
    fun dispatchNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        overScrollDelegate?.onNestedScroll(
            target as View,
            args[0] as Int,
            args[1] as Int,
            args[2] as Int,
            args[3] as Int,
            args[5] as Int
        )
        return joinPoint.proceed()
    }

    @Throws(Throwable::class)
    fun dispatchNestedPreFling(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target
        val overScrollDelegate = getOverScrollDelegate(target)
        overScrollDelegate?.onNestedPreFling(
            target as View,
            (args[0] as Float),
            (args[1] as Float)
        )
        return joinPoint.proceed()
    }

    @Throws(Throwable::class)
    fun stopNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val target = joinPoint.target
        val args = joinPoint.args
        val overScrollDelegate = getOverScrollDelegate(target)
        if (overScrollDelegate != null && args.isNotEmpty()) {
            overScrollDelegate.onStopNestedScroll((target as View), args[0] as Int)
        }
        return joinPoint.proceed()
    }
}