package com.satis.core.component.mvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStoreOwner
import com.satis.core.component.FrameworkFragment
import com.satis.viewmodel.core.BaseViewModel

/**
 * Created by sunshaobei on 2022/3/3.
 */
abstract class MVMFragment<VM : BaseViewModel>  : FrameworkFragment(),
    MVM<VM> by MVMDelegate() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initMVM(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun viewModeStoreOwner(): ViewModelStoreOwner {
        return this
    }
}