package com.satis.viewmodel.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.util.HashMap

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    private var mMutableLiveDataMap: MutableMap<String, BaseLiveData<Any?>> = HashMap(4)
    @Suppress("UNCHECKED_CAST")
    fun <T> addObserver(
        owner: LifecycleOwner,
        tag: String,
        observer: BaseObserver<T>,
        isSticky: Boolean
    ) {
        var baseLiveData = mMutableLiveDataMap[tag]
        if (baseLiveData == null) {
            baseLiveData = BaseLiveData<T>() as BaseLiveData<Any?>
            mMutableLiveDataMap[tag] = baseLiveData
        }
        baseLiveData.observe(owner, observer as Observer<Any?>, isSticky)
    }

    fun postValue(tag: String, o: Any?) {
        val baseLiveData = mMutableLiveDataMap[tag]
        baseLiveData?.postValue(o)
    }

    fun setValue(tag: String, o: Any?) {
        val baseLiveData = mMutableLiveDataMap[tag]
        baseLiveData?.setValue(o)
    }
}