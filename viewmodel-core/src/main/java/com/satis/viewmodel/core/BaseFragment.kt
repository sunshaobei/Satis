package com.satis.viewmodel.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

class BaseFragment<T:BaseViewModel>: Fragment() {
    lateinit var mViewModel:T
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val genericArrayType = this::class.java.genericSuperclass
        val actualTypeArguments = (genericArrayType as ParameterizedType).actualTypeArguments
        val viewModelClass = actualTypeArguments[0] as Class<T>
        mViewModel =  ViewModelProvider(this).get(viewModelClass)
    }
}