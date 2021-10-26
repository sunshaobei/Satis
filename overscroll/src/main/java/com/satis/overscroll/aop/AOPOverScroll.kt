package com.satis.overscroll.aop

import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
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
        val target = joinPoint.target as View
        val overScrollDelegate = getOverScrollDelegate(target)
        overScrollDelegate?.run {
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
        val target = joinPoint.target
        val b =
            (getOverScrollDelegate(target)?.getOffset(target as View) == 0 && (joinPoint.proceed()) as Boolean)
                    || dispatchNestedPreScroll(
                args,
                target
            )
        return b
    }

    private fun dispatchNestedPreScroll(
        args: Array<Any>,
        target: Any,
    ): Boolean {
        getOverScrollDelegate(target)?.onNestedPreScroll(
            target as View,
            args[0] as Int,
            args[1] as Int,
            (args[2] as IntArray),
            args[4] as Int
        )
        if ((args[2] as IntArray)[1] != 0 || (args[2] as IntArray)[0] != 0) {
            return true
        }
        return false
    }

    @Throws(Throwable::class)
    fun dispatchNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        val args = joinPoint.args
        val target = joinPoint.target as View
        val proceed = joinPoint.proceed()
        val i = (args[6] as IntArray)[1]
        val overScrollDelegate = getOverScrollDelegate(target)
        if (i == 0 || overScrollDelegate?.getOffset(target) != 0) {
            overScrollDelegate?.onNestedScroll(
                target ,
                args[0] as Int,
                args[1] as Int,
                args[2] as Int,
                args[3] as Int,
                args[5] as Int
            )
        }
        return proceed
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