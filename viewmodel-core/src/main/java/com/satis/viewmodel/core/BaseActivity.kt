package com.satis.viewmodel.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T:BaseViewModel> :AppCompatActivity(){
    lateinit var mViewModel:T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel(this::class.java)
    }
    private fun initViewModel(mClass:Class<*>){
        val genericArrayType = mClass.genericSuperclass
        if (genericArrayType is ParameterizedType){
            val actualTypeArguments = genericArrayType.actualTypeArguments
            val viewModelClass = actualTypeArguments[0] as Class<T>
            mViewModel =  ViewModelProvider(this).get(viewModelClass)
            return
        }
        val superClass = mClass.superclass
        if (superClass!=null && !superClass.name.startsWith("androidx")){
            initViewModel(superClass)
        }
    }
}