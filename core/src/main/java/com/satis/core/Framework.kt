package com.satis.core

import android.app.Application
import com.satis.core.manmager.ActivityStackManager

object Framework {
    var showActivityTag = false
    fun init(application: Application){
        AppContext.init(application)
        application.registerActivityLifecycleCallbacks(ActivityStackManager.instance)
    }
}