package com.satis.sliver.item

import com.satis.sliver.recyclerview.ViewHolder

class EmptyTypeItem(override val layoutId: Int) :BaseTypeItem{

    var itemContent: ((ViewHolder) -> Unit)?= null
}