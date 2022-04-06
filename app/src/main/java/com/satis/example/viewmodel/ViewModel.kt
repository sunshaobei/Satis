package com.satis.example.viewmodel

import android.app.Application
import android.os.Handler
import com.satis.viewmodel.apt.app.ObserveKtx.onText
import com.satis.viewmodel.core.BaseViewModel

class ViewModel(application: Application) :BaseViewModel(application) {

    init {
        Handler().postDelayed({
            setValue("onText","eemmme");
            onText("eenene")
        }, 1000)
        Handler().postDelayed({
            setValue("onText","ssss");
        }, 3000)
    }

}