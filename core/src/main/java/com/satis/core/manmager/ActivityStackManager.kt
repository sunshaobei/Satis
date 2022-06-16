package com.satis.core.manmager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import com.satis.core.component.mvm.MVMHelper
import com.satis.core.component.mvvm.MVVMHelper
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.ArrayList

class ActivityStackManager private constructor():Application.ActivityLifecycleCallbacks {
    private val mActivities = Collections.synchronizedList(ArrayList<Activity>())
    private val mActivitiesStateMap = ConcurrentHashMap<Activity,Lifecycle.Event>()
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        mActivities.add(p0)
        mActivitiesStateMap[p0] = Lifecycle.Event.ON_CREATE
        MVMHelper.checkNeedInit(p0)
        MVVMHelper.checkNeedInit(p0)
    }

    override fun onActivityStarted(p0: Activity) {
        mActivitiesStateMap[p0] = Lifecycle.Event.ON_START
    }

    override fun onActivityResumed(p0: Activity) {
        mActivitiesStateMap[p0] = Lifecycle.Event.ON_RESUME
    }

    override fun onActivityPaused(p0: Activity) {
        mActivitiesStateMap[p0] = Lifecycle.Event.ON_PAUSE
    }

    override fun onActivityStopped(p0: Activity) {
        mActivitiesStateMap[p0] = Lifecycle.Event.ON_STOP
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
        mActivities.remove(p0)
        mActivitiesStateMap.remove(p0)
    }

    fun getTopActivity():Activity{
        return mActivities[mActivities.size-1]
    }

    fun isBackground():Boolean{
        var isBackground:Boolean = true
        for ( a in mActivities){
            val event = mActivitiesStateMap[a]
            if (event != Lifecycle.Event.ON_STOP || event != Lifecycle.Event.ON_PAUSE){
                isBackground = false
                break
            }
        }
        return isBackground;
    }

    fun isForeground():Boolean{
        return !isBackground()
    }

    companion object{
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ActivityStackManager() }
    }
}