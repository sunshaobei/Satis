package com.satis.example;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Scroller;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class OverScrollDelegate {
    View mContentView;
    Scroller mScroller;
    boolean mVerticalPermit;
    public OverScrollDelegate(View contentView){
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
                float translationY = mContentView.getTranslationY();
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
        }
    }

    private void moveSpinnerInfinitely(float mOffset) {
        mContentView.setTranslationY(mOffset);
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int finalY = mScroller.getFinalY();
            if (!mContentView.canScrollVertically(-1)) {
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
