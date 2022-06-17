package com.satis.example.viewmodel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.satis.core.component.mvvm.MVVMFragment
import com.satis.example.databinding.FragmentLayoutBinding
import com.satis.viewmodel.annotation.Observe

/**
 * Created by sunshaobei on 2022/6/17.
 */
class Fragment:MVVMFragment<FragmentLayoutBinding,ViewModel>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @Observe(sticky = false)
    fun onText(s:String){
        Log.e("MVVMFragment", "test$s")
    }
}