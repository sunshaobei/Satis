package com.satis.core

import androidx.multidex.MultiDexApplication

/**
 * Created by sunshaobei on 2022/3/4.
 */
open class FrameworkApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        Framework.init(this)
    }
}