package com.satis.example;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AOPTest {

    @Before("execution(* android.view.View.computeScroll())")
    public void test(JoinPoint joinPoint){
        Log.e("sunshaobei","拦截");

        Object target = joinPoint.getTarget();
        if (target instanceof RecyclerView){
            Object tag = ((RecyclerView) target).getTag();
            if (tag == null){
                tag = new OverScrollDelegate((View) target);
                ((RecyclerView) target).setTag(tag);
            }
            ((OverScrollDelegate)tag).computeScroll();
        }
//        try {
//            joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
    }


    @Before("execution(* androidx.recyclerview.widget.RecyclerView.dispatchNestedPreFling(..))")
    public void nestedPreFling(JoinPoint joinPoint){
        Log.e("sunshaobei","拦截dispatchNestedPreFling");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            Log.e("sunshaobei","参数："+arg);
        }
        Object target = joinPoint.getTarget();
        if (target instanceof RecyclerView){
            Object tag = ((RecyclerView) target).getTag();
            if (tag == null){
                tag = new OverScrollDelegate((View) target);
                ((RecyclerView) target).setTag(tag);
            }
            ((OverScrollDelegate)tag).startFling(- (Float) args[1]);
        }
        Log.e("sunshaobei",joinPoint.getTarget().getClass().getSimpleName());
    }

}
