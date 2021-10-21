package com.satis.overscroll

import androidx.recyclerview.widget.RecyclerView
import com.satis.overscroll.api.OverScrollOffsetChangeListener


fun RecyclerView.setOnOverScrollOffsetChangeListener(listener: OverScrollOffsetChangeListener){
    this.setTag(R.id.tag_overscroll_offset_listener,listener)
}