package com.satist.overscroll.listener;

import android.content.Context;

import androidx.annotation.NonNull;

import com.satist.overscroll.api.RefreshHeader;
import com.satist.overscroll.api.RefreshLayout;

/**
 * 默认Header创建器
 */
public interface DefaultRefreshHeaderCreator {
    @NonNull
    RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout);
}
