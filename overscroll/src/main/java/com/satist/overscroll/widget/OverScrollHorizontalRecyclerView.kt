package com.satist.overscroll.widget

import android.content.Context
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.satist.overscroll.OverScrollHorizontalBehavior

class OverScrollHorizontalRecyclerView : OverScrollVerticalRecyclerView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<*> {
        return  OverScrollHorizontalBehavior()
    }
}