package com.satis.viewmodel.core;


import androidx.lifecycle.Observer;

public class ObserverWrapper<T> implements Observer<T> {
    private BaseLiveData liveData;
    private Observer<? super T> mObserver;
    private int mLastVersion;

    public ObserverWrapper(BaseLiveData liveData,Observer<? super T> observer) {
        this.liveData = liveData;
        mLastVersion = liveData.mVersion;
        mObserver = observer;
    }

    @Override
    public void onChanged(T t) {
        //此处做拦截操作
        if (mLastVersion>=liveData.mVersion){
            return;
        }
        mObserver.onChanged(t);
    }
}