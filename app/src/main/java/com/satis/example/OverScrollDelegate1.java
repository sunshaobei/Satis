package com.satis.example;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;


public class OverScrollDelegate1 {
    View mContentView;
    Scroller mScroller;
    boolean mVerticalPermit;
    public OverScrollDelegate1(View contentView){
        mContentView = contentView;
        mScroller = new Scroller(contentView.getContext());
    }
    Handler mHandler = new android.os.Handler();


    Runnable r = new Runnable() {
        @Override
        public void run() {
            computeScroll();
            ViewCompat.postOnAnimation(mContentView, this);
        }
    };


    public void startFling(float velocity){
        mVerticalPermit = false;//关闭竖直通行证
        mScroller.fling(0, 0, 0, (int) -velocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        mScroller.computeScrollOffset();
        ViewCompat.postOnAnimation(mContentView, r);
    }

    public void onStopNestedScroll(@NonNull View target) {
        overBack();
    }

    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        final int dy = dyUnconsumed ;
        if ((dy < 0 && !mContentView.canScrollVertically(-1))||(!mContentView.canScrollVertically(1) && dy> 0)) {
            moveSpinnerInfinitely(mTranslationY -= dy);
        }
    }

    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
//        int consumedY = 0;
//        if (dy * mTranslationY > 0) {
//            if (Math.abs(dy) > Math.abs(mTranslationY)) {
//                consumedY = (int) mTranslationY;
//                mTranslationY = 0;
//            } else {
//                consumedY = dy;
//                mTranslationY -= dy;
//            }
//            moveSpinnerInfinitely(mTranslationY);
//        } else if (dy > 0 ) {
//            consumedY = dy;
//            mTranslationY -= dy;
//            moveSpinnerInfinitely(mTranslationY);
//        }
//        consumed[1] += consumedY;
    }


    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        startFling(-velocityY);
        return false;
    }


    protected class BounceRunnable implements Runnable {
        int mFrame = 0;
        int mFrameDelay = 10;
        int mSmoothDistance;
        long mLastTime;
        float mOffset = 0;
        float mVelocity;

        BounceRunnable(float velocity, int smoothDistance) {
            mVelocity = velocity;
            mSmoothDistance = smoothDistance;
            mLastTime = AnimationUtils.currentAnimationTimeMillis();
            mHandler.postDelayed(this, mFrameDelay);
        }

        @Override
        public void run() {
            mVelocity *= Math.pow(0.85f, ++mFrame * 2);//回弹滚动数度衰减
            long now = AnimationUtils.currentAnimationTimeMillis();
            float t = 1f * (now - mLastTime) / 1000;
            float velocity = mVelocity * t;
            if (Math.abs(velocity) >= 1) {
                mLastTime = now;
                mOffset += velocity;
                moveSpinnerInfinitely(mOffset);
                mHandler.postDelayed(this, mFrameDelay);
            }else {
//                overBack();
            }
        }
    }

    private void overBack(){
        float translationY = mContentView.getTranslationY();
        if (translationY==0){
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(translationY, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mContentView.setTranslationY(animatedValue);
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    private float mTranslationY;

    private void moveSpinnerInfinitely(float mOffset) {
        mTranslationY = mOffset;
        mContentView.setTranslationY(mOffset);
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int finalY = mScroller.getFinalY();
            if (!mContentView.canScrollVertically(-1) ||! mContentView.canScrollVertically(1)) {
                if(mVerticalPermit) {
                    float velocity;
                    velocity = finalY > 0 ? -mScroller.getCurrVelocity() : mScroller.getCurrVelocity();
                    new BounceRunnable(velocity,0);
                }
                mScroller.forceFinished(true);
            } else {
                mVerticalPermit = true;//打开竖直通行证
//                ViewCompat.postOnAnimation(mContentView, r);
            }
        }
    }
}
