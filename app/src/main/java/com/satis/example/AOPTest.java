package com.satis.example;
//
import android.util.Log;
import android.view.View;

import androidx.core.view.ViewParentCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.satis.overscroll.OverScrollDelegate;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
@Aspect
public class AOPTest {
    @Around("execution(void android.view.ViewGroup.computeScroll(..))")
    public void test(ProceedingJoinPoint joinPoint) throws Throwable {
//        Log.e("sunshaobei","拦截");

//        Object target = joinPoint.getTarget();
//        if (target instanceof RecyclerView){
//            Object tag = ((RecyclerView) target).getTag();
//            if (tag == null){
//                tag = new OverScrollDelegate((View) target);
//                ((RecyclerView) target).setTag(tag);
//            }
////            ((OverScrollDelegate)tag).computeScroll();
//        }
        joinPoint.proceed();
    }
    
    private OverScrollDelegate getOverScrollDelegate(Object target){
        if (target instanceof RecyclerView){
            Object tag = ((RecyclerView) target).getTag(R.id.tag_overscroll_delegate);
            if (tag == null){
                tag = new OverScrollDelegate((View) target);
                ((RecyclerView) target).setTag(R.id.tag_overscroll_delegate,tag);
            }
            return (OverScrollDelegate) tag;
        }
        return null;
    }


    @Around("execution(* androidx.recyclerview.widget.RecyclerView.dispatchNestedPreFling(..))")
    public Object dispatchNestedPreFling(ProceedingJoinPoint joinPoint){
        Log.e("sunshaobei","拦截 dispatchNestedPreFling");
        Object[] args = joinPoint.getArgs();
        Object b = null;
        try {
            b =  joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        Object target = joinPoint.getTarget();
        OverScrollDelegate overScrollDelegate = getOverScrollDelegate(target);
        if (overScrollDelegate!=null){
            overScrollDelegate.onNestedPreFling((View) target,(Float) args[1]);
        }
        return b;
    }

    @Around("execution(* androidx.recyclerview.widget.RecyclerView.dispatchNestedScroll(..))")
    public Object dispatchNestedScroll(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("sunshaobei","拦截 dispatchNestedScroll");
        Object[] args = joinPoint.getArgs();
//        for (Object arg : args) {
//            Log.e("sunshaobei","参数："+arg);
//        }
        Object target = joinPoint.getTarget();
        OverScrollDelegate overScrollDelegate = getOverScrollDelegate(target);
        if (overScrollDelegate!=null){
            overScrollDelegate.onNestedScroll((View)target, (int)args[0],(int)args[1],(int)args[2],(int)args[3],(int)args[5]);
        }
        return joinPoint.proceed();
    }


    @Around("execution(* androidx.recyclerview.widget.RecyclerView.startNestedScroll(..))")
    public Object startNestedScroll(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("sunshaobei","拦截 startNestedScroll");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
//            Log.e("sunshaobei","参数："+arg);
        }

        Object target = joinPoint.getTarget();
        OverScrollDelegate overScrollDelegate = getOverScrollDelegate(target);
        if (overScrollDelegate!=null){
            if (overScrollDelegate.onStartNestedScroll((int)args[0], (int)args[1])) {
                overScrollDelegate.onNestedScrollAccepted((View) target,(int)args[0], (int)args[1]);
                return true;
            }
        }
        return joinPoint.proceed();
    }

    @Around("execution(* androidx.recyclerview.widget.RecyclerView.stopNestedScroll(..))")
    public Object stopNestedScroll(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("sunshaobei","拦截 stopNestedScroll");
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        OverScrollDelegate overScrollDelegate = getOverScrollDelegate(target);
        if (overScrollDelegate!=null&& args.length>0){
            overScrollDelegate.onStopNestedScroll((View) target,(int)args[0]);
        }
        return joinPoint.proceed();
    }

    @Around("execution(* androidx.recyclerview.widget.RecyclerView.dispatchNestedPreScroll(..))")
    public Object dispatchNestedPreScroll(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("sunshaobei","拦截 dispatchNestedPreScroll");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
//            Log.e("sunshaobei","参数："+arg);
        }
        Object target = joinPoint.getTarget();
        OverScrollDelegate overScrollDelegate = getOverScrollDelegate(target);
        if (overScrollDelegate!=null){
            overScrollDelegate.onNestedPreScroll((View) target,(int)args[0],(int) args[1],(int[]) args[2],(int)args[4]);
        }
        return joinPoint.proceed();
    }
}
