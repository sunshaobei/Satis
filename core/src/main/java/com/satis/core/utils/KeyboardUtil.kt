package com.satis.core.utils

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object KeyboardUtil {

    fun show(view:View,show:Boolean = true){
        ViewCompat.getWindowInsetsController(view)?.run {
            if (show){
                show(WindowInsetsCompat.Type.ime())
            }else{
                hide(WindowInsetsCompat.Type.ime())
            }
        }
    }


    fun hide(view:View){
        show(view,false)
    }
}