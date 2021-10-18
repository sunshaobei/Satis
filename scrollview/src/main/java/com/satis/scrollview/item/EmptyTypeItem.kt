package com.satis.scrollview.item

import android.view.View
import com.satis.scrollview.recyclerview.ViewHolder

class EmptyTypeItem(override val layoutId: Int) :BaseTypeItem{

    var itemContent: ((ViewHolder) -> Unit)?= null
}