package com.satis.overscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

/**
 * 垂直方向上的弹性滑动和惯性滑动效果
 *
 * @author ziwei huang
 */
public class OverScrollVerticalBehavior extends BaseOverScrollBehavior {

    public OverScrollVerticalBehavior() {
    }

    public OverScrollVerticalBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, View child,
                                       View directTargetChild, View target, int nestedScrollAxes, int type) {
        if (child != target) {
            return false;
        }

        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child,
                                  View target, int dx, int dy, int[] consumed, int type) {
        consumed[1] = onNestedPreScrollInner(coordinatorLayout, child, target, dy, type);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type) {
        onNestedScrollInner(coordinatorLayout, child, target, dyConsumed, dyUnconsumed, type);

    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return onNestedPreFlingInner(coordinatorLayout, child, target, velocityY);
    }

    @Override
    public void updateOffset(View child, int offset) {
//        child.setTranslationY(offset);
        child.setPadding(0,offset,0,0);
    }

    @Override
    public int getOffset(View child) {
        return (int) child.getPaddingTop();
//        return (int) child.getTranslationY();
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