package com.satis.present

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup

class PresentDelegate : Present {

    private lateinit var target: Activity
    private lateinit var contentView: ViewGroup
    private lateinit var blackView: View
    private val content: View by lazy {
        contentView.getChildAt(0)
    }

    override fun initTarget(activity: Activity) {
        this.target = activity
        contentView = target.findViewById(android.R.id.content)
        blackView = View(target)
        blackView.setBackgroundColor(Color.BLACK)
    }


    override fun statusBarAnimate(animatorValue: Float) {

    }

    override fun backStackAnimator(animatorValue: Float) {
        val layoutParams = content.layoutParams as ViewGroup.MarginLayoutParams
        contentView.addView(blackView, 0, ViewGroup.LayoutParams(-1, -1))
        layoutParams.topMargin = 40
        content.scaleX = 0.9f
    }

}