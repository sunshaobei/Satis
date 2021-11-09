package com.satis.binding.core

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.satis.binding.core.factory.GradientDrawableFactory
import kotlin.jvm.JvmOverloads
import com.satis.binding.core.factory.StateListDrawableFactory
import com.satis.core.utils.SizeUtil.dp2px

object DefaultBindAdapter {
    @BindingAdapter(value = ["imageUrl", "placeDrawableId", "errorDrawableId"])
    fun setImageUrl(
        imageView: ImageView?,
        imageUrl: String?,
        placeDrawableId: Int,
        errorDrawableId: Int
    ) {
    }

    /**
     * shape [GradientDrawable.RECTANGLE]
     * 分别为 0，1，2，3
     *
     * @param view
     * @param solid
     */
    @BindingAdapter(
        value = ["shape", "solid", "strokeWidth", "strokeColor", "radius", "radiusLeftTop", "radiusLeftBottom", "radiusRightTop", "radiusRightBottom"],
        requireAll = false
    )
    fun gradientDrawable(
        view: View,
        shape: Int,
        solid: Int,
        strokeWidth: Float,
        strokeColor: Int,
        radius: Float,
        radiusLeftTop: Float,
        radiusLeftBottom: Float,
        radiusRightTop: Float,
        radiusRightBottom: Float
    ) {
        val floats: FloatArray
        floats = if (radius > 0) {
            floatArrayOf(dp2px(radius))
        } else {
            floatArrayOf(
                dp2px(radiusLeftTop), dp2px(radiusLeftTop),
                dp2px(radiusRightTop), dp2px(radiusRightTop),
                dp2px(radiusRightBottom), dp2px(radiusRightBottom),
                dp2px(radiusLeftBottom), dp2px(radiusLeftBottom)
            )
        }
        view.background =
            GradientDrawableFactory.createDrawable(shape, solid, strokeWidth, strokeColor, *floats)
    }

    /**
     * 需要press 状态的背景
     * @param view
     * @param unPressDrawable
     * @param pressDrawable
     */
    @BindingAdapter(value = ["unPressDrawableId", "pressDrawableId"])
    fun pressDrawable(view: View, unPressDrawable: Int, pressDrawable: Int) {
        view.background =
            StateListDrawableFactory.createPressDrawable(unPressDrawable, pressDrawable)
    }

    /**
     * 需要press 状态的背景
     * @param view
     * @param unPressDrawable
     * @param pressDrawable
     */
    @BindingAdapter(value = ["unPressDrawable", "pressDrawable"])
    fun pressDrawable(view: View, unPressDrawable: Drawable?, pressDrawable: Drawable?) {
        view.background =
            StateListDrawableFactory.createPressDrawable(unPressDrawable, pressDrawable)
    }

    /**
     * 类似checkbox 场景
     * @param view
     * @param unCheckDrawable
     * @param checkDrawable
     */
    @BindingAdapter(value = ["unCheckDrawableId", "checkDrawableId"])
    fun checkDrawable(view: View, unCheckDrawable: Int, checkDrawable: Int) {
        view.background =
            StateListDrawableFactory.createPressDrawable(unCheckDrawable, checkDrawable)
    }

    /**
     * 类似checkbox 场景
     * @param view
     * @param unCheckDrawable
     * @param checkDrawable
     */
    @BindingAdapter(value = ["unCheckDrawable", "checkDrawable"])
    fun checkDrawable(view: View, unCheckDrawable: Drawable?, checkDrawable: Drawable?) {
        view.background =
            StateListDrawableFactory.createPressDrawable(unCheckDrawable, checkDrawable)
    }

    /**
     * 背景选中状态
     * @param view
     * @param unSelectDrawable
     * @param selectDrawable
     */
    @BindingAdapter(value = ["unSelectDrawableId", "selectDrawableId"])
    fun selectDrawable(view: View, unSelectDrawable: Int, selectDrawable: Int) {
        view.background =
            StateListDrawableFactory.createSelectDrawable(unSelectDrawable, selectDrawable)
    }

    /**
     * 背景选中状态
     * @param view
     * @param unSelectDrawable
     * @param selectDrawable
     */
    @BindingAdapter(value = ["unSelectDrawable", "selectDrawable"])
    fun selectDrawable(view: View, unSelectDrawable: Drawable?, selectDrawable: Drawable?) {
        view.background =
            StateListDrawableFactory.createSelectDrawable(unSelectDrawable, selectDrawable)
    }

    /**
     * text 设置删除线
     *
     * @param view
     */
    @BindingAdapter("deleteLine")
    fun setTextViewDeleteLine(view: TextView, delete: Boolean) {
        if (delete) {
            // 方式1
            view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            // 方式2
            // view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    /**
     * text 设置下划线
     * @param view
     */
    @BindingAdapter("underLine")
    fun setTextViewUnderLine(view: TextView, drawUnderLine: Boolean) {
        if (drawUnderLine) {
            // 方式1
            view.paintFlags = view.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            // 方式2
            // view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }
}