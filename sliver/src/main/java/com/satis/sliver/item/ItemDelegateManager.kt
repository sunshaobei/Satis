package com.satis.sliver.item

import androidx.collection.ArrayMap
import androidx.databinding.ViewDataBinding
import com.satis.sliver.TypeSelector
import com.satis.sliver.recyclerview.ViewHolder
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

/**
 * Created by sunsh on 18/5/30.
 */
class ItemDelegateManager {
    //    var delegates: SparseArrayCompat<TypeItem?> = SparseArrayCompat()
    var itemMap = ArrayMap<Int, TypeItem>()
    var typeMap = ArrayMap<Int, TypeItem>()
    var selector: TypeSelector<Any>? = null
    val itemViewDelegateCount: Int
        get() = itemMap.size

    fun addDelegate(layout: Int, baseTypeItem: BaseTypeItem): ItemDelegateManager {
        val viewType = itemMap.size
        addDelegate(viewType, layout, baseTypeItem)
        return this
    }

    fun addDelegate(viewType: Int, layout: Int, baseTypeItem: BaseTypeItem): ItemDelegateManager {
        require(itemMap[layout] == null) {
            ("An ItemViewDelegate is already registered for the viewType = "
                    + viewType
                    + ". Already registered ItemViewDelegate is "
                    + itemMap[layout])
        }
        itemMap[layout] = TypeItem(viewType, baseTypeItem)
        typeMap[viewType] = TypeItem(layout, baseTypeItem)
        return this
    }

    fun removeDelegate(baseTypeItem: BaseTypeItem?): ItemDelegateManager {
        if (baseTypeItem == null) {
            throw NullPointerException("ItemViewDelegate is null")
        }
        for (i in 0 until itemMap.size) {
            val typeItem = itemMap.valueAt(i)
            if (typeItem!!.baseTypeItem === baseTypeItem) {
                itemMap.removeAt(i)
                typeMap.removeAt(i)
                break
            }
        }
        return this
    }

    fun removeDelegate(itemType: Int): ItemDelegateManager {
        val indexToRemove = itemMap.indexOfKey(itemType)
        if (indexToRemove >= 0) {
            itemMap.removeAt(indexToRemove)
            typeMap.removeAt(indexToRemove)
        }
        return this
    }

    fun getItemViewType(item: Any, position: Int): Int {
        if (itemMap.size == 0){
            throw IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=$position in data source"
            )
        }
        return if (itemMap.size == 1) {
            typeMap.keyAt(0)
        } else {
            if (selector == null){
                throw IllegalArgumentException(
                    "TypeSelector didn't set"
                )
            }
            val layoutId = selector!!.invoke(item, position)
            val type = itemMap[layoutId]?.tag
                ?: throw IllegalArgumentException(
                    "No ItemViewDelegateManager added that matches position=$position in data source"
                )
            type
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun convert(holder: ViewHolder, item: Any, position: Int) {
        if (itemMap.size == 0)
            throw IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=$position in data source"
            )
        val itemViewType = getItemViewType(item, position)
        val baseTypeItem = typeMap[itemViewType]!!.baseTypeItem
        if (baseTypeItem is ListTypeItem<*>){
            baseTypeItem as ListTypeItem<Any>
            baseTypeItem.itemContent!!.invoke(item,position,holder)
            return
        }else if (baseTypeItem is ListTypeBindingItem<*,*>){
            baseTypeItem as ListTypeBindingItem<Any,ViewDataBinding>
            baseTypeItem.itemContent!!.invoke(item,position,holder.getBinding<ViewDataBinding>()!!)
            holder.getBinding<ViewDataBinding>()!!.executePendingBindings()
        }
    }

    fun getItemViewDelegate(viewType: Int): BaseTypeItem {
        return typeMap[viewType]!!.baseTypeItem
    }

    fun getItemViewLayoutId(viewType: Int): Int {
        return getItemViewDelegate(viewType).layoutId
    }
}