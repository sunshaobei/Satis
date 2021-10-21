package com.satis.overscroll

import androidx.annotation.IntDef
import com.satis.overscroll.api.OverScrollImp

@IntDef(
    OverScrollImp.DIRECTION_UP,
    OverScrollImp.DIRECTION_DOWN,
    OverScrollImp.DIRECTION_LEFT,
    OverScrollImp.DIRECTION_RIGHT
)
annotation class ScrollDirection 