package com.satis.core.utils

import com.satis.core.AppContext.context


object SizeUtil {

    fun dp2px(dp: Float): Float {
        val scale: Float =context.resources.displayMetrics.density
        return (dp * scale + 0.5f)
    }
    inline val Float.dp: Float get() = dp2px(this)
    inline val Int.dp: Float get() = dp2px(this.toFloat())
    inline val Double.dp: Float get() = dp2px(this.toFloat())
}