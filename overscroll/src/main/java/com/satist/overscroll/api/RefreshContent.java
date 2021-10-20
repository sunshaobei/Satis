package com.satist.overscroll.api;

import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.satist.overscroll.listener.ScrollBoundaryDecider;

/**
 * 刷新内容组件
 */
public interface RefreshContent {

    @NonNull
    View getView();
    @NonNull
    View getScrollableView();

    void onActionDown(MotionEvent e);

    void setUpComponent(RefreshKernel kernel, View fixedHeader, View fixedFooter);
    void setScrollBoundaryDecider(ScrollBoundaryDecider boundary);

    void setEnableLoadMoreWhenContentNotFull(boolean enable);

    void moveSpinner(int spinner, int headerTranslationViewId, int footerTranslationViewId);

    boolean canRefresh();
    boolean canLoadMore();

    AnimatorUpdateListener scrollContentWhenFinished(int spinner);
}
