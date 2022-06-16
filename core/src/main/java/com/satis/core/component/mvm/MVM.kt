package com.satis.core.component.mvm


import androidx.lifecycle.ViewModelStoreOwner
import com.satis.viewmodel.core.BaseViewModel

/**
 * Created by sunshaobei on 2022/6/15.
 */
interface MVM<VM: BaseViewModel> {
    var viewModel:VM
    fun initMVM(storeOwner: ViewModelStoreOwner)
}