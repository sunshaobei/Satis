package com.satis.viewmodel.core;


import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public interface Observer {
    void observe(@NonNull LifecycleOwner owner);
}
