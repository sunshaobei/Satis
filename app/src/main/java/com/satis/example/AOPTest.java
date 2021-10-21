package com.satis.example;
//
//import android.util.Log;
//import android.view.View;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//
//@Aspect
public class AOPTest {
//
//    @Around("execution(void android.view.ViewGroup.computeScroll(..))")
//    public void test(ProceedingJoinPoint joinPoint) throws Throwable {
////        Log.e("sunshaobei","拦截");
//
//        Object target = joinPoint.getTarget();
//        if (target instanceof RecyclerView){
//            Object tag = ((RecyclerView) target).getTag();
//            if (tag == null){
//                tag = new OverScrollDelegate((View) target);
//                ((RecyclerView) target).setTag(tag);
//            }
////            ((OverScrollDelegate)tag).computeScroll();
//        }
//        joinPoint.proceed();
//    }
//
//
//    @Around("execution(* androidx.recyclerview.widget.RecyclerView.dispatchNestedPreFling(..))")
//    public Object dispatchNestedPreFling(ProceedingJoinPoint joinPoint){
//        Log.e("sunshaobei","拦截 dispatchNestedPreFling");
//        Object[] args = joinPoint.getArgs();
////        for (Object arg : args) {
////            Log.e("sunshaobei","参数："+arg);
////        }
//        Object b = null;
//        try {
//            b =  joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        Object target = joinPoint.getTarget();
//        if (target instanceof RecyclerView){
//            Object tag = ((RecyclerView) target).getTag();
//            if (tag == null){
//                tag = new OverScrollDelegate((View) target);
//                ((RecyclerView) target).setTag(tag);
//            }
//            if (b instanceof Boolean){
//                if (!(Boolean) b){
//                    ((OverScrollDelegate)tag).onNestedPreFling((View) target,(Float) args[0], (Float) args[1]);
//                }
//            }
//        }
//        return b;
//    }
//
//    @Around("execution(* androidx.recyclerview.widget.RecyclerView.dispatchNestedScroll(..))")
//    public Object dispatchNestedScroll(ProceedingJoinPoint joinPoint){
//        Log.e("sunshaobei","拦截 dispatchNestedScroll");
//        Object[] args = joinPoint.getArgs();
////        for (Object arg : args) {
////            Log.e("sunshaobei","参数："+arg);
////        }
//        Object target = joinPoint.getTarget();
//        if (target instanceof RecyclerView){
//            Object tag = ((RecyclerView) target).getTag();
//            if (tag == null){
//                tag = new OverScrollDelegate((View) target);
//                ((RecyclerView) target).setTag(tag);
//            }
//            ((OverScrollDelegate)tag).onNestedScroll((View)target, (int)args[0],(int)args[1],(int)args[2],(int)args[3]);
//        }
////        Log.e("sunshaobei",joinPoint.getTarget().getClass().getSimpleName());
//        Object b = null;
//        try {
//            b =  joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return b;
//    }
//
//
//    @Around("execution(* androidx.recyclerview.widget.RecyclerView.startNestedScroll(..))")
//    public Object startNestedScroll(ProceedingJoinPoint joinPoint){
//        Log.e("sunshaobei","拦截 startNestedScroll");
//        Object[] args = joinPoint.getArgs();
//        for (Object arg : args) {
////            Log.e("sunshaobei","参数："+arg);
//        }
////        Object target = joinPoint.getTarget();
////        if (target instanceof RecyclerView){
////            Object tag = ((RecyclerView) target).getTag();
////            if (tag == null){
////                tag = new OverScrollDelegate((View) target);
////                ((RecyclerView) target).setTag(tag);
////            }
////            ((OverScrollDelegate)tag).onStartNestedScroll();
////        }
////        Log.e("sunshaobei",joinPoint.getTarget().getClass().getSimpleName());
//        Object b = null;
//        try {
//            b =  joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return b;
//    }
//
//    @Around("execution(* androidx.recyclerview.widget.RecyclerView.stopNestedScroll(..))")
//    public Object stopNestedScroll(ProceedingJoinPoint joinPoint){
//        Log.e("sunshaobei","拦截 stopNestedScroll");
//        Object target = joinPoint.getTarget();
//        if (target instanceof RecyclerView){
//            Object tag = ((RecyclerView) target).getTag();
//            if (tag == null){
//                tag = new OverScrollDelegate((View) target);
//                ((RecyclerView) target).setTag(tag);
//            }
//            ((OverScrollDelegate)tag).onStopNestedScroll((View) target);
//        }
//        Object b = null;
//        try {
//            b =  joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return b;
//    }
//
//    @Around("execution(* androidx.recyclerview.widget.RecyclerView.dispatchNestedPreScroll(..))")
//    public Object dispatchNestedPreScroll(ProceedingJoinPoint joinPoint){
//        Log.e("sunshaobei","拦截 dispatchNestedPreScroll");
//        Object[] args = joinPoint.getArgs();
//        for (Object arg : args) {
////            Log.e("sunshaobei","参数："+arg);
//        }
//        Object target = joinPoint.getTarget();
//        if (target instanceof RecyclerView){
//            Object tag = ((RecyclerView) target).getTag();
//            if (tag == null){
//                tag = new OverScrollDelegate((View) target);
//                ((RecyclerView) target).setTag(tag);
//            }
//            ((OverScrollDelegate)tag).onNestedPreScroll((View) target,(int)args[0],(int) args[1],(int[]) args[2]);
//        }
//        Log.e("sunshaobei","dy："+args[1]);
//        Log.e("sunshaobei","消费前："+((int[])args[2])[1]);
//        Object b = null;
//        try {
//            b =  joinPoint.proceed();
//            Log.e("sunshaobei","是否消费："+b);
//            Log.e("sunshaobei","消费后："+((int[])args[2])[1]);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return b;
//    }
}
