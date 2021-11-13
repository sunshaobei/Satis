package com.satis.binding.core.factory

import android.R
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import com.satis.core.AppContext.context

object StateListDrawableFactory {
    /**
     * 从 drawable 获取图片 id 给 Imageview 添加 selector
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun createPressDrawable(unPress: Int, pressed: Int): StateListDrawable {
        val drawable = StateListDrawable()
        val normal: Drawable = context.resources.getDrawable(unPress,context.theme)
        val press: Drawable = context.resources.getDrawable(pressed,context.theme)
        drawable.addState(intArrayOf(R.attr.state_pressed), press)
        drawable.addState(intArrayOf(-R.attr.state_pressed), normal)
        return drawable
    }

    /**
     * 从 drawable 获取图片 id 给 Imageview 添加 selector
     */
    fun createPressDrawable(unPress: Drawable?, pressed: Drawable?): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(R.attr.state_pressed), pressed)
        drawable.addState(intArrayOf(-R.attr.state_pressed), unPress)
        return drawable
    }

    /**
     * checkedDrawable
     * @param unCheck
     * @param check
     * @return
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun createCheckDrawable(unCheck: Int, check: Int): StateListDrawable {
        val drawable = StateListDrawable()
        val normal: Drawable = context.resources.getDrawable(unCheck,context.theme)
        val press: Drawable = context.resources.getDrawable(check,context.theme)
        drawable.addState(intArrayOf(R.attr.state_checked), press)
        drawable.addState(intArrayOf(-R.attr.state_checked), normal)
        return drawable
    }

    /**
     * checkedDrawable
     * @param unCheck
     * @param check
     * @return
     */
    @SuppressLint
    fun createCheckDrawable(unCheck: Drawable?, check: Drawable?): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(R.attr.state_checked), check)
        drawable.addState(intArrayOf(-R.attr.state_checked), unCheck)
        return drawable
    }

    /**
     * selectDrawable
     * @param unSelect
     * @param select
     * @return
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun createSelectDrawable(unSelect: Int, select: Int): StateListDrawable {
        val drawable = StateListDrawable()
        val normal: Drawable = context.resources.getDrawable(unSelect,context.theme)
        val press: Drawable = context.resources.getDrawable(select,context.theme)
        drawable.addState(intArrayOf(R.attr.state_selected), press)
        drawable.addState(intArrayOf(-R.attr.state_selected), normal)
        return drawable
    }

    /**
     * selectDrawable
     * @param unSelect
     * @param select
     * @return
     */
    fun createSelectDrawable(unSelect: Drawable?, select: Drawable?): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(R.attr.state_selected), select)
        drawable.addState(intArrayOf(-R.attr.state_selected), unSelect)
        return drawable
    }
}