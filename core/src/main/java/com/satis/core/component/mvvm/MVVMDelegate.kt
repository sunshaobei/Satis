package com.satis.core.component.mvvm

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelStoreOwner
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.satis.viewmodel.core.SatisViewModel
import java.lang.Exception
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * Created by sunshaobei on 2022/3/3.
 * MVVM 初始化代理类
 */
class MVVMDelegate<T : ViewDataBinding, K : AndroidViewModel> : MVVM<T, K> {
    private lateinit var viewModelStoreOwner: ViewModelStoreOwner
    override lateinit var binding: T
    override lateinit var viewModel: K
    override fun initTarget(viewModelStoreOwner: ViewModelStoreOwner) {
        this.viewModelStoreOwner = viewModelStoreOwner
        //初始化 binding 与 viewmodel
        mvvmInit()
    }

    @Suppress("UNCHECKED_CAST")
    private fun mvvmInit() {
        val type = viewModelStoreOwner.javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = type.actualTypeArguments
        // init viewBinding
        val dataBindingClass = actualTypeArguments[0] as Class<T>
        var method: Method?
        try {
            method = dataBindingClass.getMethod("inflate", LayoutInflater::class.java)
            method.isAccessible = true
            val layoutInflater = layoutInflater
            binding = (method.invoke(dataBindingClass, layoutInflater) as T)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // init viewModel
        val viewModelClass = actualTypeArguments[1] as Class<K>
        viewModel = (ViewModelProvider(fragmentViewModelOwner()!!)[viewModelClass])
        SatisViewModel.observe(viewModelStoreOwner as LifecycleOwner)
    }

    private fun fragmentViewModelOwner(): ViewModelStoreOwner? {
        return if (viewModelStoreOwner is MVVMFragment<*, *>) {
            (viewModelStoreOwner as MVVMFragment<*, *>).viewModeStoreOwner()
        } else viewModelStoreOwner
    }

    private val layoutInflater: LayoutInflater?
        get() {
            var layoutInflater: LayoutInflater? = null
            if (viewModelStoreOwner is MVVMActivity<*, *>) {
                layoutInflater = (viewModelStoreOwner as MVVMActivity<*, *>).layoutInflater
            } else if (viewModelStoreOwner is MVVMFragment<*, *>) {
                layoutInflater = (viewModelStoreOwner as MVVMFragment<*, *>).layoutInflater
            }
            return layoutInflater
        }

}