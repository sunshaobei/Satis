package com.satis.viewmodel.core;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.Map;

public class BaseViewModel extends AndroidViewModel {
    Map<String,BaseLiveData> mMutableLiveDataMap = new HashMap<>(4);
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public <T> void addObserver(@NonNull LifecycleOwner owner, String tag, BaseObserver<T> observer, boolean isSticky){
        BaseLiveData baseLiveData = mMutableLiveDataMap.get(tag);
        if (baseLiveData==null){
            baseLiveData = new BaseLiveData<T>();
            mMutableLiveDataMap.put(tag,baseLiveData);
        }
        baseLiveData.observe(owner, (Observer) observer, isSticky);
    }

    public void postValue(String tag,Object o){
        BaseLiveData baseLiveData = mMutableLiveDataMap.get(tag);
        if (baseLiveData!=null){
            baseLiveData.postValue(o);
        }
    }
    public void setValue(String tag,Object o){
        BaseLiveData baseLiveData = mMutableLiveDataMap.get(tag);
        if (baseLiveData!=null){
            baseLiveData.setValue(o);
        }
    }

}
