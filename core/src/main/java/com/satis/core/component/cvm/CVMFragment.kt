package com.satis.core.component.cvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelStoreOwner
import com.satis.core.component.FrameworkFragment
import com.satis.viewmodel.core.BaseViewModel

/**
 * Created by sunshaobei on 2022/6/8.
 */
abstract class CVMFragment: FrameworkFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeContent()
            }
        }
    }
    @Composable
    abstract fun ComposeContent()
}