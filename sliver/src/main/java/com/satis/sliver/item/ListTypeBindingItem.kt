package com.satis.sliver.item

class ListTypeBindingItem<T,K> (override val layoutId: Int) :BaseTypeItem{
    var itemContent: ((item: T, position: Int,K) -> Unit) ?= null
}