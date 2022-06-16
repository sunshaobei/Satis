package com.satis.core.component.mvvm

import android.app.Activity
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 初始化 binding、viewModel 辅助
 * Created by sunshaobei on 2022/3/3.
 * MVVM 初始化代理类
 */
object MVVMHelper {
    fun checkNeedInit(activity: Activity){
        if (activity is MVVM<*,*>){
            activity.initMVVM(activity as ViewModelStoreOwner)
        }
    }
}