package com.satis.present

import android.app.Activity

interface Present {

    fun initTarget(activity: Activity)

    fun statusBarAnimate(animatorValue:Float)

    fun backStackAnimator(animatorValue: Float)

}