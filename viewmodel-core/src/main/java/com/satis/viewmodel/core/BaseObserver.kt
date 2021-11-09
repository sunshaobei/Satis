package com.satis.viewmodel.core

import androidx.lifecycle.Observer

class BaseObserver<T>(private val observer: Observer<T>) : Observer<T?> {
    override fun onChanged(t: T?) {
        observer.onChanged(t)
    }

}