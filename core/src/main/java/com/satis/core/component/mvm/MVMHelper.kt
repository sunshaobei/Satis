package com.satis.core.component.mvm

import android.app.Activity
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 初始化 viewModel 辅助
 * Created by sunshaobei on 2022/6/15.
 */
object MVMHelper {
    fun checkNeedInit(activity: Activity){
        if (activity is MVM<*>){
            activity.initMVM(activity as ViewModelStoreOwner)
        }
    }
}