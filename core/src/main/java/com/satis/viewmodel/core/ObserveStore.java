package com.satis.viewmodel.core;

import androidx.lifecycle.LifecycleOwner;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;

public interface ObserveStore {
   LinkedHashMap<Class<? extends LifecycleOwner>, Constructor<? extends Observer>> getStore();
}
