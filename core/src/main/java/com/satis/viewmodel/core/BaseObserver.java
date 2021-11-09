package com.satis.viewmodel.core;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

public class BaseObserver<T> implements Observer<T> {
    private Observer<T> observer;

    public BaseObserver(Observer<T> observer) {
        this.observer = observer;
    }
    @Override
    public void onChanged(@Nullable T t) {
        observer.onChanged(t);
    }
}
