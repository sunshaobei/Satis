package com.satis.binding.core.factory

import android.graphics.drawable.GradientDrawable
import com.satis.binding.core.factory.GradientDrawableFactory
import kotlin.jvm.JvmOverloads
import com.satis.binding.core.factory.StateListDrawableFactory
import com.satis.core.utils.SizeUtil.dp

/**
 * 为避免创建过多的资源文件，现建议使用代码创建backgroud 图
 */
object GradientDrawableFactory {
    fun createDrawable(solid: Int): GradientDrawable {
        return createDrawable(GradientDrawable.RECTANGLE, solid, 0f, 0)
    }

    fun createDrawable(solid: Int, vararg radius: Float): GradientDrawable {
        return createDrawable(GradientDrawable.RECTANGLE, solid, 0f, 0, *radius)
    }

    fun createDrawable(shape: Int, solid: Int, vararg radius: Float): GradientDrawable {
        return createDrawable(shape, solid, 0f, 0, *radius)
    }

    fun createDrawable(solid: Int, strokeWidth: Float, strokeColor: Int): GradientDrawable {
        return createDrawable(GradientDrawable.RECTANGLE, solid, strokeWidth, strokeColor)
    }

    @JvmOverloads
    fun createDrawable(
        shape: Int,
        solid: Int,
        strokeWidth: Float = 0f,
        strokeColor: Int = 0
    ): GradientDrawable {
        return createDrawable(shape, solid, strokeWidth, strokeColor)
    }

    fun createDrawable(
        shape: Int,
        solid: Int,
        strokeWidth: Float,
        strokeColor: Int,
        vararg radius: Float
    ): GradientDrawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(solid)
        gradientDrawable.shape = shape
        if (strokeWidth > 0) {
            gradientDrawable.setStroke(strokeWidth.dp.toInt(), strokeColor)
        }
        if (radius != null) {
            if (radius.size == 1) {
                gradientDrawable.cornerRadius = radius[0]
            } else {
                gradientDrawable.cornerRadii = radius
            }
        }
        return gradientDrawable
    }
}