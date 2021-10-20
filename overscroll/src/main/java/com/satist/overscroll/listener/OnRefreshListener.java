package com.satist.overscroll.listener;


import androidx.annotation.NonNull;

import com.satist.overscroll.api.RefreshLayout;

/**
 * 刷新监听器
 */
public interface OnRefreshListener {
    void onRefresh(@NonNull RefreshLayout refreshLayout);
}
