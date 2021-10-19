package com.satis.sliver.item
import com.satis.sliver.recyclerview.ViewHolder

class ListTypeItem<T> (override val layoutId: Int) :BaseTypeItem{
    var itemContent: ((item: T, position: Int, ViewHolder) -> Unit) ?= null
}