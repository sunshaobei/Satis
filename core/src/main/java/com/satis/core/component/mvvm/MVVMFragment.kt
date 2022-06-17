package com.satis.core.component.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelStoreOwner
import com.satis.core.component.FrameworkFragment
import com.satis.viewmodel.core.BaseViewModel

/**
 * Created by sunshaobei on 2022/3/3.
 */
abstract class MVVMFragment<T : ViewDataBinding, K : BaseViewModel>  : FrameworkFragment(),
    MVVM<T, K> by MVVMDelegate() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initMVVM(this)
        return binding.root
    }

    open fun viewModeStoreOwner(): ViewModelStoreOwner {
        return requireActivity()
    }
}