package com.satis.core.component.mvm

import com.satis.core.component.FrameworkActivity
import com.satis.viewmodel.core.BaseViewModel

/**
 * Created by sunshaobei on 2022/3/3.
 */
abstract class MVMActivity<VM : BaseViewModel> : FrameworkActivity(),
    MVM<VM> by MVMDelegate() {
}