package com.satis.core.component.mvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.satis.viewmodel.core.BaseViewModel
import com.satis.viewmodel.core.SatisViewModel
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 初始化 viewModel 辅助
 * Created by sunshaobei on 2022/6/15.
 */
class MVMDelegate<VM : BaseViewModel> : MVM<VM> {
    override lateinit var viewModel: VM

    @Suppress("UNCHECKED_CAST")
    override fun initMVM(storeOwner: ViewModelStoreOwner) {
        val interfaces: Array<Type> = storeOwner.javaClass.genericInterfaces
        interfaces.forEach {
            if (it is ParameterizedType) {
                if (it.toString().startsWith(MVM::class.java.name) ) {
                    val actualTypeArguments = it.actualTypeArguments
                    val viewModelClass = actualTypeArguments[0] as Class<VM>
                    viewModel = (ViewModelProvider(storeOwner)[viewModelClass])
                    SatisViewModel.observe(storeOwner as LifecycleOwner)
                }
            }
        }
    }
}