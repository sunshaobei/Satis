package com.satis.viewmodel.core

import androidx.lifecycle.LifecycleOwner

interface Observer {
    fun observe(owner: LifecycleOwner)
}