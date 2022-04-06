package com.satis.core.component.mvvm

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Created by sunshaobei on 2022/3/3.
 * mvvm 初始 代理接口
 */
interface MVVM<T : ViewDataBinding, K : AndroidViewModel> {
    var binding :T
    var viewModel:K
    fun initTarget(viewModelStoreOwner: ViewModelStoreOwner)
}