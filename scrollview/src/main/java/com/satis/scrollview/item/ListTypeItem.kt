package com.satis.scrollview.item
import com.satis.scrollview.recyclerview.ViewHolder

class ListTypeItem<T> (override val layoutId: Int) :BaseTypeItem{
    var itemContent: ((item: T, position: Int, ViewHolder) -> Unit) ?= null
}