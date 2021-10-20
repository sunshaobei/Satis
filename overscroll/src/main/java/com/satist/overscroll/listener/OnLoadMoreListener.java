package com.satist.overscroll.listener;

import androidx.annotation.NonNull;

import com.satist.overscroll.api.RefreshLayout;

/**
 * 加载更多监听器
 */
public interface OnLoadMoreListener {
    void onLoadMore(@NonNull RefreshLayout refreshLayout);
}
