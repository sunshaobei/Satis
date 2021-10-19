package com.satis.sliver

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.satis.sliver.recyclerview.ViewHolder
import com.satis.sliver.recyclerview.wrapper.LoadMoreStateManager

/**
 *  第二、三参数分别为：在recyclerview的实际位置 与 List 数据索引位置
 */

typealias LoadMoreCallback = LoadMoreStateManager.()->Unit
typealias ItemClick = (ViewHolder, Int,Int)->Unit
typealias BindingItemClick = (ViewDataBinding,Int,Int)->Unit
typealias ItemLongClick = (ViewHolder, Int,Int)->Boolean
typealias BindingItemLongClick = (ViewDataBinding,Int,Int)->Boolean
typealias SpanSizeCallback = (GridLayoutManager,GridLayoutManager.SpanSizeLookup?,Int) -> Int
typealias TypeSelector<T> =  (T, Int) -> Int
