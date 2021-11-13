package com.satis.example.viewmodel

import android.app.Application
import android.os.Handler
import com.satis.viewmodel.core.BaseViewModel

class ViewModel(application: Application) :BaseViewModel(application) {

    init {
        Handler().postDelayed({
            setValue("test","eemmme");
        }, 1000)
        Handler().postDelayed({
            setValue("sssss","ssss");
        }, 3000)
    }
}