package com.satis.viewmodel.core;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class BaseLiveData<T> extends MutableLiveData<T> {

    public int mVersion = -1;

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer, boolean isSticky) {
        if(isSticky){
            super.observe(owner, observer);
        } else {
            super.observe(owner,new ObserverWrapper<T>(this,observer));
        }
    }

    @Override
    public void setValue(T value) {
        mVersion++;
        super.setValue(value);
    }

    @Override
    public void postValue(T value) {
        mVersion++;
        super.postValue(value);
    }
}