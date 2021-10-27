package com.satis.overscroll

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.satis.overscroll.api.RefreshImp


typealias RefreshCallback = () -> Unit
typealias OverScrollOffsetChangeCallback = (View, Int) -> Unit

fun RecyclerView.addOverScrollOffsetChangeListener(listener: OverScrollOffsetChangeCallback) {
    var tag = this.getTag(R.id.tag_overscroll_offset_listener)
    if (tag == null) {
        tag = ArrayList<OverScrollOffsetChangeCallback>()
        setTag(R.id.tag_overscroll_refresh_listener,tag)
    }
    (tag as ArrayList<OverScrollOffsetChangeCallback>).add(listener)
}

fun RecyclerView.removeOverScrollOffsetChangeListener(listener: OverScrollOffsetChangeCallback) {
    var tag = this.getTag(R.id.tag_overscroll_offset_listener)
    if (tag != null) {
        (tag as ArrayList<OverScrollOffsetChangeCallback>).remove(listener)
    }

}


fun RecyclerView.setOnRefreshListener(refreshImp: RefreshImp?, refreshCallback: RefreshCallback?) {
    setTag(R.id.tag_overscroll_refresh,refreshImp)
    setTag(R.id.tag_overscroll_refresh_listener,refreshCallback)
}


fun RecyclerView.refreshComplete() {
    val tag = getTag(R.id.tag_overscroll_delegate)
    if (tag != null) {
        val overScrollDelegate = tag as OverScrollDelegate
        overScrollDelegate.refreshComplete()
    }
}