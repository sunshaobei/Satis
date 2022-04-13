package com.satis.sliver.item

import com.satis.sliver.TypeSelector
import com.satis.sliver.recyclerview.ViewHolder

interface TypeItem<T> {
    var selector: TypeSelector<T>?
    var viewHolder: ViewHolder
}