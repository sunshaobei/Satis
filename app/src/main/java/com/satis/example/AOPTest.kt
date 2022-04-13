package com.satis.example

import android.graphics.Canvas
import com.satis.overscroll.Constants
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Around
import kotlin.Throws
import org.aspectj.lang.ProceedingJoinPoint
import com.satis.overscroll.aop.AOPOverScroll

@Aspect
open class AOPTest {

//    @Around( "execution(* androidx.recyclerview.widget.RecyclerView.draw(..))")
//    @Throws(Throwable::class)
//    fun draw(joinPoint: ProceedingJoinPoint){
//        val args = joinPoint.args
//        val canvas = args[0] as Canvas
//        canvas.save()
//        canvas.translate(0f,100f)
//        joinPoint.proceed()
//        canvas.restore()
//    }

    @Around(Constants.STARTNESTEDSCROLL)
    @Throws(Throwable::class)
    fun startNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        return AOPOverScroll.startNestedScroll(joinPoint)
    }

    @Around(Constants.DISPATCHNESTEDPRESCROLL)
    @Throws(Throwable::class)
    fun dispatchNestedPreScroll(joinPoint: ProceedingJoinPoint): Any? {
        return AOPOverScroll.dispatchNestedPreScroll(joinPoint)
    }

    @Around(Constants.DISPATCHNESTEDSCROLL)
    @Throws(Throwable::class)
    fun dispatchNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        return AOPOverScroll.dispatchNestedScroll(joinPoint)
    }

    @Around(Constants.DISPATCHNESTEDPREFLING)
    @Throws(Throwable::class)
    fun dispatchNestedPreFling(joinPoint: ProceedingJoinPoint): Any? {
        return AOPOverScroll.dispatchNestedPreFling(joinPoint)
    }

    @Around(Constants.STOPNESTEDSCROLL)
    @Throws(Throwable::class)
    fun stopNestedScroll(joinPoint: ProceedingJoinPoint): Any? {
        return AOPOverScroll.stopNestedScroll(joinPoint)
    }
}