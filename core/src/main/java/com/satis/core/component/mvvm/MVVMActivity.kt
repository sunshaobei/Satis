package com.satis.core.component.mvvm
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.satis.core.component.FrameworkActivity
import com.satis.viewmodel.core.BaseViewModel

/**
 * Created by sunshaobei on 2022/3/3.
 */
abstract class MVVMActivity<T : ViewDataBinding, K : BaseViewModel> : FrameworkActivity(),
    MVVM<T, K> by MVVMDelegate() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTarget(this)
        super.setContentView(binding.root)
    }


    override fun setContentView(layoutResID: Int) {
        throw IllegalAccessException("FrameworkActivity<T:ViewDataBinding> 不支持setContentView 调用")
    }

    override fun setContentView(view: View) {
        throw IllegalAccessException("FrameworkActivity<T:ViewDataBinding> 不支持setContentView 调用")
    }
}