package com.satis.example.viewmodel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.satis.core.component.mvvm.MVVMActivity
import com.satis.example.R
import com.satis.example.databinding.ActivitySatisViewModelBinding
import com.satis.viewmodel.annotation.Observe
import com.satis.viewmodel.app.ObserveStore_app
import com.satis.viewmodel.core.SatisViewModel

class SatisViewModelActivity :MVVMActivity<ActivitySatisViewModelBinding,ViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SatisViewModel.addObserverStore(ObserveStore_app())
        viewModel.get()
        supportFragmentManager.beginTransaction().add(R.id.container,Fragment()).commit()
    }

    override fun onResume() {
        super.onResume()
    }

    @Observe
    fun onText(s:String){
        Log.e("SatisViewModelActivity", "test$s")
    }

    fun click(view: View) {
        startActivity(Intent(this,TestActivity::class.java))
    }
}