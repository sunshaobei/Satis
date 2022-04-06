package com.satis.core.utils

import com.satis.core.AppContext.context

/**
 * Created by sunshaobei on 2022/4/2.
 */
object ScreenUtil {
    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth():Int{
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取屏幕高度
     */
    fun getScreenHeight():Int{
        return context.resources.displayMetrics.heightPixels
    }
}