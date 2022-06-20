package com.satis.example

import com.satis.core.Framework
import com.satis.core.FrameworkApplication
import com.satis.present.PresentStackManager
import com.satis.viewmodel.app.ObserveStore_app
import com.satis.viewmodel.core.SatisViewModel

/**
 * Created by sunshaobei on 2022/6/17.
 */
class SampleApplication :FrameworkApplication(){

    override fun onCreate() {
        super.onCreate()
        SatisViewModel.addObserverStore(ObserveStore_app())
        registerActivityLifecycleCallbacks(PresentStackManager())
    }
}