package com.satis.example.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.satis.example.R
import com.satis.viewmodel.annotation.Observe
import com.satis.viewmodel.apt.app.ObserveStore
import com.satis.viewmodel.core.BaseActivity
import com.satis.viewmodel.core.SatisViewModel

class SatisViewModelActivity :BaseActivity<ViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satis_view_model)
        SatisViewModel.addObserverStore(ObserveStore())
        SatisViewModel.observe(this)
        startActivity(Intent(this,TestActivity::class.java))
    }

    @Observe(tag = "test")
    fun onText(s:String){
        Log.e("sunshaobei",s)
    }
}