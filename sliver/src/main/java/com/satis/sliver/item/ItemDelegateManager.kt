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
    private var itemMap = ArrayMap<TypeItem<Any>, Int>()
    private var typeMap = ArrayMap<Int, TypeItem<Any>>()
    private var typeSelectorMap = ArrayMap<TypeSelector<Any>, Int>()
    val selectors by lazy {
        ArrayList<TypeSelector<Any>>()
    }
    val itemViewDelegateCount: Int
        get() = itemMap.size

    fun <T> addDelegate(typeItem: TypeItem<T>): ItemDelegateManager {
        val viewType = itemMap.size
        addDelegate(viewType, typeItem)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> addDelegate(viewType: Int, typeItem: TypeItem<T>): ItemDelegateManager {
        require(typeMap[viewType] == null) {
            ("An ItemViewDelegate is already registered for the viewType = "
                    + viewType
                    + ". Already registered ItemViewDelegate is "
                    + typeMap[viewType])
        }
        typeMap[viewType] = typeItem as TypeItem<Any>
        typeItem.selector?.let {
            selectors.add(it)
            typeSelectorMap[it] = viewType
        }
        itemMap[typeItem] = viewType
        return this
    }

    fun removeDelegate(typeItem: TypeItem<Any>?): ItemDelegateManager {
        if (typeItem == null) {
            throw NullPointerException("ItemViewDelegate is null")
        }
        val indexOfKey = itemMap.indexOfKey(typeItem)
        if (indexOfKey >= 0) {
            itemMap.removeAt(indexOfKey)
            typeMap.removeAt(indexOfKey)
            typeSelectorMap.removeAt(indexOfKey)
        }
        return this
    }

    fun removeDelegate(itemType: Int): ItemDelegateManager {
        val indexToRemove = typeMap.indexOfKey(itemType)
        if (indexToRemove >= 0) {
            itemMap.removeAt(indexToRemove)
            typeMap.removeAt(indexToRemove)
            typeSelectorMap.removeAt(indexToRemove)
        }
        return this
    }

    fun getItemViewType(item: Any, position: Int): Int {
        if (itemMap.size == 0) {
            throw IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=$position in data source"
            )
        }
        return if (itemMap.size == 1) {
            typeMap.keyAt(0)
        } else {
            if (selectors.isEmpty()) {
                throw IllegalArgumentException(
                    "TypeSelector didn't set"
                )
            }
            selectors.forEach {
                if (it.invoke(item)) {
                    return typeSelectorMap[it]!!
                }
            }
            throw IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=$position in data source"
            )

        }
    }

    @Suppress("UNCHECKED_CAST")
    fun convert(holder: ViewHolder, item: Any, position: Int) {
        if (itemMap.size == 0)
            throw IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=$position in data source"
            )
        val itemViewType = getItemViewType(item, position)
        val typeItem = typeMap[itemViewType]!!
        if (typeItem is ListTypeItem<*>) {
            typeItem as ListTypeItem<Any>
            typeItem.itemContent?.invoke(item, position, holder)
            return
        } else if (typeItem is ListTypeBindingItem<*, *>) {
            typeItem as ListTypeBindingItem<Any, ViewDataBinding>
            typeItem.itemContent?.invoke(item,
                position,
                holder.getBinding<ViewDataBinding>()!!)
            holder.getBinding<ViewDataBinding>()!!.executePendingBindings()
        }
    }

    fun getItemViewDelegate(viewType: Int): TypeItem<Any> {
        return typeMap[viewType]!!
    }
}