package com.satis.overscroll;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;

public  class OverScrollDelegate implements IOverScroll {

    private static final int MAX_BOUNCE_BACK_DURATION_MS = 300;
    private static final int MIN_BOUNCE_BACK_DURATION_MS = 150;

    private IOverScrollCallback mDefaultOverCallback = new SimpleOverScrollCallback();

    private final Interpolator mSpringBackInterpolator = new DecelerateInterpolator(0.8f);

    private ValueAnimator mSpringBackAnimator;
    private OverScroller mOverScroller;

    private @IOverScrollCallback.ScrollDirection
    int mDirectionToEnd, mDirectionToStart;
    private View mContentView;

    public OverScrollDelegate(View contentView) {
        mContentView = contentView;
    }

    public  boolean onStartNestedScroll( int nestedScrollAxes, int type){
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    public void onNestedScrollAccepted( @NonNull View target, int axes, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            stopSpringBack(target);
        }

        if (type == ViewCompat.TYPE_TOUCH) {
            if (mOverScroller != null) {
                mOverScroller.forceFinished(true);
            }
        }

        if ((axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0) {
            mDirectionToEnd = IOverScrollCallback.DIRECTION_DOWN;
            mDirectionToStart = IOverScrollCallback.DIRECTION_UP;
        } else {
            mDirectionToEnd = IOverScrollCallback.DIRECTION_RIGHT;
            mDirectionToStart = IOverScrollCallback.DIRECTION_LEFT;
        }
    }

    public void onNestedPreScroll(
                                  View target, int dx, int dy, int[] consumed, int type) {
        consumed[1] = onNestedPreScrollInner(target, dy, type);
    }

    public void onNestedScroll(
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type) {
        onNestedScrollInner( target, dyConsumed, dyUnconsumed, type);
    }

    /**
     * @return consumed distance
     */
    protected int onNestedPreScrollInner(View target, int distance, int type) {
        if (distance != 0) {
            int min, max;
            if (distance < 0) { // We're scrolling to end
                if (!mDefaultOverCallback.canScroll(this, target, mDirectionToEnd)) {
                    return 0;
                }

                min = getOffset(target);
                max = 0;
            } else {  // We're scrolling to start
                if (!mDefaultOverCallback.canScroll(this, target, mDirectionToStart)) {
                    return 0;
                }

                min = 0;
                max = getOffset(target);
            }
            if (min != max) {
                return scrollWithoutDampingFactor(target, distance, min, max);
            }
        }

        return 0;
    }


    protected void onNestedScrollInner(View target, int distanceConsumed, int distanceUnconsumed,
                                       int type) {

        if (distanceUnconsumed != 0) { // fix nested scroll bugs
            mContentView.getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (distanceUnconsumed < 0) {
            // If the scrolling view is scrolling to end but not consuming, it's probably be at
            // the top of it's content

            if (!mDefaultOverCallback.canScroll(this, target, mDirectionToEnd)) {
                return;
            }

            if (type == ViewCompat.TYPE_TOUCH) {
                scroll(target, distanceUnconsumed, 0, getMaxOffset(target));
            } else { // fling
                if (mOverScroller == null
                        || !mOverScroller.computeScrollOffset()
                        || Math.abs(mOverScroller.getCurrVelocity()) < Math.abs(mDefaultOverCallback.getMinFlingVelocity(this, target, mDirectionToEnd))  // too slow
                        || getOffset(target) >= mDefaultOverCallback.getMaxFlingOffset(this, target, mDirectionToEnd)) { // reach edge
                    ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
                } else {
                    scroll(target, distanceUnconsumed,
                            getOffset(target), mDefaultOverCallback.getMaxFlingOffset(this, target, mDirectionToEnd));
                }
            }

        } else if (distanceUnconsumed > 0) {
            if (!mDefaultOverCallback.canScroll(this, target, mDirectionToStart)) {
                return;
            }

            if (type == ViewCompat.TYPE_TOUCH) {
                scroll(target, distanceUnconsumed, getMinOffset(target), 0);
            } else { // fling
                if (mOverScroller == null
                        || !mOverScroller.computeScrollOffset()
                        || Math.abs(mOverScroller.getCurrVelocity()) < Math.abs(mDefaultOverCallback.getMinFlingVelocity(this, target, mDirectionToStart))  // too slow
                        || getOffset(target) <= mDefaultOverCallback.getMaxFlingOffset(this, target, mDirectionToStart)) { // reach edge
                    ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
                } else {
                    scroll(target, distanceUnconsumed,  // slow down
                            mDefaultOverCallback.getMaxFlingOffset(this, target, mDirectionToStart), getOffset(target));
                }
            }
        }
    }

    public boolean onNestedPreFling(View target, float velocity) {
        if (mOverScroller == null) {
            mOverScroller = new OverScroller(mContentView.getContext());
        }
        /* velocityX = 0, velocityY = velocity
                    or
           velocityX = velocity, velocityY = 0
         */
        mOverScroller.fling(0, 0, 0, (int) velocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        return false;
    }

    public void onStopNestedScroll(View target, int type) {

        if (type == ViewCompat.TYPE_TOUCH) { // touching
            if (getOffset(target) != 0) { // and out of bound
                if( mOverScroller == null || !mOverScroller.computeScrollOffset()) { // no fling
                    springBack(target);
                }
            }
        } else {
            if (getOffset(target) != 0) {
                springBack(target);
            }
        }
    }

    /**
     *
     */
    private final int computerWithDampingFactor(View child, int distance) {
        int direction = distance > 0 ? mDirectionToStart : mDirectionToEnd;
        float factor = mDefaultOverCallback.getDampingFactor(this, child, direction);
        if (factor == 0) {
            factor = 1;
        }
        int newDistance = (int) (distance / factor + 0.5f);
        return newDistance;
    }

    private final int scrollWithoutDampingFactor(View child, int distance, int minOffset, int maxOffset) {
        return computerOffset(child, getOffset(child) - distance, minOffset, maxOffset);
    }

    private final int scroll(View child, int distance, int minOffset, int maxOffset) {
        return computerOffset(child, getOffset(child) - computerWithDampingFactor(child, distance), minOffset, maxOffset);
    }

    /**
     * @return 消耗掉距离
     */
    private int computerOffset(View child, int newOffset, int minOffset, int maxOffset) {
        final int curOffset = getOffset(child);
        int consumed = 0;

        if (curOffset >= minOffset && curOffset <= maxOffset) {
            // If we have some scrolling range, and we're currently within the min and max
            // offsets, calculate a new offset
            newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);

            if (curOffset != newOffset) {
                setOffset(child, newOffset);
                // Update how much dy we have consumed
                consumed = curOffset - newOffset;
            }
        }

        return consumed;
    }

    @Override
    public void stopSpringBack(View child) {
        if (mSpringBackAnimator != null) {
            if (mSpringBackAnimator.isRunning()) {
                mSpringBackAnimator.cancel();
            }
        }

        mDefaultOverCallback.onStopSpringingBack(this, child);

    }

    @Override
    public void springBack(final View child) {
        int startOffset = getOffset(child);
        if (startOffset == 0) {
            return;
        }

        if (mDefaultOverCallback.onSpringBack(this, child)) {
            return;
        }

        if (mSpringBackAnimator == null) {
            mSpringBackAnimator = ValueAnimator.ofInt();
            mSpringBackAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    setOffset(child, value);
                }
            });
        }

        if (mSpringBackAnimator.isStarted()) {
            return;
        }


        float bounceBackDuration = (Math.abs(startOffset) * 1f / getMaxOffset(child)) * MAX_BOUNCE_BACK_DURATION_MS;
        mSpringBackAnimator.setDuration(Math.max((int) bounceBackDuration, MIN_BOUNCE_BACK_DURATION_MS));
        mSpringBackAnimator.setInterpolator(mSpringBackInterpolator);
        mSpringBackAnimator.setIntValues(startOffset, 0);
        mSpringBackAnimator.start();
    }

    @Override
    public void setOffset(View child, int offset) {
        updateOffset(child, offset);
        mDefaultOverCallback.onOffsetChanged(this, child, getOffset(child));
    }

    public void updateOffset(View child, int offset) {
        child.setTranslationY(offset);
    }

    @Override
    public int getOffset(View child) {
        return (int) child.getTranslationY();
    }

    @Override
    public int getMaxOffset(View child) {
        return child.getHeight();
    }

    @Override
    public int getMinOffset(View child) {
        return -child.getHeight();
    }
}