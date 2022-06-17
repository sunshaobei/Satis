package com.satis.example.viewmodel

import android.app.Application
import com.satis.viewmodel.app.ObserveKtx.onText
import com.satis.viewmodel.core.BaseViewModel

class ViewModel(application: Application) :BaseViewModel(application) {

    fun get(){
        onText("eenenenensdfsdf是的发送到发深爱的")
    }


}