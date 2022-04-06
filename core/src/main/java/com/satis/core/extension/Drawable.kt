package com.satis.core.extension

import android.R
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import com.satis.core.AppContext.context

/**
 * Created by sunshaobei on 2022/3/31.
 */
object Drawable {
    fun getRipple():Int{
        val attrs = intArrayOf(R.attr.selectableItemBackgroundBorderless)
        val typedArray = context.obtainStyledAttributes(attrs)
        val backgroundResource = typedArray.getResourceId(0, 0)
        typedArray.recycle()
        return backgroundResource
    }

    fun View.setRipple(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.background = ContextCompat.getDrawable(context, getRipple())
        }
    }
}