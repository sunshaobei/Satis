package com.satis.core.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.satis.core.AppContext.context
import com.satis.core.manmager.ActivityStackManager

/**
 * Created by sunshaobei on 2022/4/2.
 */
object StatusBarUtil {

    fun show( show: Boolean = true){
        ViewCompat.getWindowInsetsController( ActivityStackManager.instance.getTopActivity().window.decorView)?.run {
            if (show){
                show(WindowInsetsCompat.Type.statusBars())
            }else{
                hide(WindowInsetsCompat.Type.statusBars())
            }
        }
    }
    fun hide(){
        show(false)
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(): Int {
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     *  状态栏文字颜色黑色
     */
    fun light(light:Boolean = true){
        ViewCompat.getWindowInsetsController( ActivityStackManager.instance.getTopActivity().window.decorView)?.run {
            isAppearanceLightStatusBars = light
        }
    }

    /**
     *  状态栏文字颜色白色
     */
    fun dark(){
        light(false)
    }

}