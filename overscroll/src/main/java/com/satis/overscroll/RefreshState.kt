package com.satis.overscroll

import androidx.annotation.IntDef
import com.satis.overscroll.api.OverScrollImp
import com.satis.overscroll.api.RefreshImp

@IntDef(
    RefreshImp.PULL_DOWN,
    RefreshImp.PULL_DOWN_OVER,
    RefreshImp.REFRESHING,
    RefreshImp.RELEASE,
)
annotation class RefreshState