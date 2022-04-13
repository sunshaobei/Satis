package com.satis.sliver.item
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.satis.sliver.TypeSelector
import com.satis.core.AppContext.context
import com.satis.sliver.recyclerview.ViewHolder

class ListTypeItem<T>: TypeItem<T> {
    private var viewHolderCreateBlock:()-> ViewHolder
    override var selector: TypeSelector<T>? = null
    override var viewHolder: ViewHolder
    get() {
        return viewHolderCreateBlock.invoke()
    }
    set(value) {}

    constructor(itemLayoutId: Int, typeSelector: TypeSelector<T>?, parent:RecyclerView) {
        viewHolderCreateBlock = {
            ViewHolder.createViewHolder(LayoutInflater.from(context).inflate(itemLayoutId,parent,false))
        }
        selector = typeSelector
    }
    constructor(itemView: View, typeSelector: TypeSelector<T>?, parent:RecyclerView) {
        viewHolderCreateBlock = {
            ViewHolder.createViewHolder(itemView)
        }
        selector = typeSelector
    }
    var itemContent: ((item: T, position: Int, ViewHolder) -> Unit) ?= null
}