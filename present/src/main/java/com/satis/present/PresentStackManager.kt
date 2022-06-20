package com.satis.present

import android.app.Activity
import android.app.Application
import android.os.Bundle

class PresentStackManager :Application.ActivityLifecycleCallbacks{

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is Present){
            activity.initTarget(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is Present){
            activity.backStackAnimator(1f)
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}