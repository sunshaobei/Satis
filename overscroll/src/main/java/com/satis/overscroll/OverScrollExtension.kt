package com.satis.overscroll

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.satis.overscroll.api.OverScrollOffsetChangeListener


typealias RefreshCallback = ()->Unit

fun RecyclerView.setOnOverScrollOffsetChangeListener(listener: OverScrollOffsetChangeListener){
    this.setTag(R.id.tag_overscroll_offset_listener,listener)
}


fun RecyclerView.setOnRefreshListener(refresh:View){

}

fun RecyclerView.refreshComplete(){

}