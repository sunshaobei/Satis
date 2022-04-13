package com.satis.sliver.item

import android.view.LayoutInflater
import android.view.View
import com.satis.sliver.TypeSelector
import com.satis.core.AppContext.context
import com.satis.sliver.recyclerview.ViewHolder

class EmptyTypeItem : TypeItem<Any> {
    override var selector: TypeSelector<Any>? = null
    private var viewHolderCreateBlock:()-> ViewHolder
    override var viewHolder: ViewHolder
        get() {
            return viewHolderCreateBlock.invoke()
        }
        set(value) {}

    constructor(itemLayoutId: Int){
        viewHolderCreateBlock = {
            ViewHolder.createViewHolder(LayoutInflater.from(context).inflate(itemLayoutId,null))
        }
    }
    constructor(itemView: View){
        viewHolderCreateBlock = {
            ViewHolder.createViewHolder(itemView)
        }
    }
    var itemContent: ((ViewHolder) -> Unit)?= null
}