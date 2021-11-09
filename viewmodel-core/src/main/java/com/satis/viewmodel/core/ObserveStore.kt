package com.satis.viewmodel.core

import androidx.lifecycle.LifecycleOwner
import java.lang.reflect.Constructor
import java.util.*

interface ObserveStore {
    val store: LinkedHashMap<Class<out LifecycleOwner>, Constructor<out Observer>>
}