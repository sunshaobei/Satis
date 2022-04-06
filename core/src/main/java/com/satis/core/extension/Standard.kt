package com.satis.core.extension

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object Standard {
    fun Any.onUIThread(delay:Long = 0,runnable: Runnable){
        GlobalHandler.instance.postDelayed(runnable,delay)
    }

    inline fun Any.IOThread(crossinline block:()->Unit){
        CoroutineScope(Dispatchers.IO).launch {
            block()
        }
    }

    inline infix fun ViewDataBinding.addToTarget(activity: AppCompatActivity){
        activity.setContentView(this.root)
    }
}

class GlobalHandler : Handler(Looper.getMainLooper()){
    companion object{
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { GlobalHandler() }
    }
}