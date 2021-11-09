package com.satis.viewmodel.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class BaseLiveData<T> : MutableLiveData<T>() {
    var mVersion = -1
    fun observe(owner: LifecycleOwner, observer: Observer<T>, isSticky: Boolean) {
        if (isSticky) {
            super.observe(owner, observer)
        } else {
            super.observe(owner, ObserverWrapper(this, observer))
        }
    }

    override fun setValue(value: T) {
        mVersion++
        super.setValue(value)
    }

    override fun postValue(value: T) {
        mVersion++
        super.postValue(value)
    }
}