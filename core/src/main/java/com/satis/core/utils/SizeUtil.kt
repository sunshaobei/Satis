package com.satis.core.utils
import com.satis.core.AppContext.context

object SizeUtil {
    fun dp2px(dp: Float): Float {
        val scale: Float =context.resources.displayMetrics.density
        return (dp * scale + 0.5f)
    }

    inline val Int.dp: Float get() = dp2px(this.toFloat())

    inline val Float.dp: Float get() = dp2px(this)

    inline val Double.dp: Float get() = dp2px(this.toFloat())

    inline val Int.dpInt:Int get() = dp2px(this.toFloat()).toInt()

    inline val Float.dpInt:Int get() = dp2px(this).toInt()

    inline val Double.dpInt:Int get() = dp2px(this.toFloat()).toInt()

}