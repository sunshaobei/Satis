package com.satist.overscroll.listener;

import android.content.Context;

import androidx.annotation.NonNull;

import com.satist.overscroll.api.RefreshFooter;
import com.satist.overscroll.api.RefreshLayout;

/**
 * 默认Footer创建器
 */
public interface DefaultRefreshFooterCreator {
    @NonNull
    RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout);
}
