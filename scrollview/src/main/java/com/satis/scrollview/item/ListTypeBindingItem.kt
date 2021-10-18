package com.satis.scrollview.item
import com.satis.scrollview.recyclerview.ViewHolder

class ListTypeBindingItem<T,K> (override val layoutId: Int) :BaseTypeItem{
    var itemContent: ((item: T, position: Int,K) -> Unit) ?= null
}