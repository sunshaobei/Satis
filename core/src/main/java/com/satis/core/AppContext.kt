package com.satis.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Created sunshaobei on 19/697/109923.
 */
@SuppressLint("StaticFieldLeak")
object AppContext {
    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    lateinit var application: Application
        private set

    /**
     * 初始化工具类
     *
     * @param application 上下文
     */
    @JvmStatic
    fun init(application: Application) {
        this.application = application
    }

    inline val Any.context: Context get() = application
}