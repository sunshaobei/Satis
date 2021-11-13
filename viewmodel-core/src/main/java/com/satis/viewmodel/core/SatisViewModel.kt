package com.satis.viewmodel.core

import androidx.lifecycle.LifecycleOwner
import java.lang.reflect.Constructor
import java.util.LinkedHashMap

object SatisViewModel {
    private val OBSERVERS: LinkedHashMap<Class<out LifecycleOwner>, Constructor<out Observer>> =
        LinkedHashMap()

    fun observe(lifecycleOwner: LifecycleOwner) {
        observe(lifecycleOwner, lifecycleOwner::class.java)
    }

    fun addObserverStore(observerStore: ObserveStore) {
        OBSERVERS.putAll(observerStore.store)
    }

    private fun observe(
        lifecycleOwner: LifecycleOwner,
        ownerClass: Class<out LifecycleOwner>
    ) {
        var observeImp: Constructor<out Observer>? = OBSERVERS[ownerClass]
        //递归查询 父类订阅方法 由于是 activity 与fragment 所以 supperclass 没必要校验空
        val superclassName = ownerClass.superclass.name
        if (!superclassName.startsWith("androidx.") && superclassName != "BaseActivity") {
            ownerClass.superclass?.let {
                observe(
                    lifecycleOwner,
                    it as Class<out LifecycleOwner>
                )
            }
        }
        observeImp?.newInstance()?.observe(lifecycleOwner)
    }
}