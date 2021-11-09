package com.satis.viewmodel.core

import androidx.lifecycle.Observer
import com.satis.viewmodel.core.BaseLiveData
import com.satis.viewmodel.core.BaseObserver

class ObserverWrapper<T>(private val liveData: BaseLiveData<*>, observer: Observer<in T>) :
    Observer<T> {
    private val mObserver: Observer<in T> = observer
    private val mLastVersion: Int = liveData.mVersion
    override fun onChanged(t: T) {
        //此处做拦截操作
        if (mLastVersion >= liveData.mVersion) {
            return
        }
        mObserver.onChanged(t)
    }

}