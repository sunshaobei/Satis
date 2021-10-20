package com.satist.overscroll.util;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ScrollingView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.satist.overscroll.api.RefreshComponent;
import com.satist.overscroll.api.RefreshKernel;
import com.satist.overscroll.listener.CoordinatorLayoutListener;

/**
 * Design 兼容包缺省尝试
 * Created by scwang on 2018/1/29.
 */
public class WidgetUtil {

    public static void checkCoordinatorLayout(View content, RefreshKernel kernel, final CoordinatorLayoutListener listener) {
        try {//try 不能删除，不然会出现兼容性问题
            if (content instanceof CoordinatorLayout) {
                kernel.getRefreshLayout().setEnableNestedScroll(false);
                ViewGroup layout = (ViewGroup) content;
                for (int i = layout.getChildCount() - 1; i >= 0; i--) {
                    View view = layout.getChildAt(i);
                    if (view instanceof AppBarLayout) {
                        ((AppBarLayout) view).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                            @Override
                            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                                listener.onCoordinatorUpdate(
                                        verticalOffset >= 0,
                                        (appBarLayout.getTotalScrollRange() + verticalOffset) <= 0);
                            }
                        });
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public static boolean isScrollableView(View view) {
        if (view instanceof RefreshComponent) {
            return false;
        }
        return view instanceof AbsListView
                || view instanceof ScrollView
                || view instanceof ScrollingView
                || view instanceof WebView
                || view instanceof NestedScrollingChild;
    }

    public static boolean isContentView(View view) {
        if (view instanceof RefreshComponent) {
            return false;
        }
        return isScrollableView(view)
                || view instanceof ViewPager
                || view instanceof NestedScrollingParent;
    }

}
