package com.satis.sliver.recyclerview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.satis.sliver.item.ItemDelegateManager
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.satis.sliver.*
import com.satis.sliver.item.BaseTypeItem

/**
 * Created by sunsh on 18/5/30.
 */
open class BaseMultiItemTypeAdapter(var mContext: Context, var datas: List<Any>) :
    RecyclerView.Adapter<ViewHolder>() {
    var mItemDelegateManager: ItemDelegateManager = ItemDelegateManager()
    private var mItemClickEnable = false
    var mItemClick: ItemClick? = null
        set(value) {
            mItemClickEnable = true
            field = value
        }
    var mItemLongClick: ItemLongClick? = null
        set(value) {
            mItemClickEnable = true
            field = value
        }
    var mBindingItemClick: BindingItemClick? = null
        set(value) {
            mItemClickEnable = true
            field = value
        }
    var mBindingItemLongClick: BindingItemLongClick? = null
        set(value) {
            mItemClickEnable = true
            field = value
        }

    fun setTypeSelector(typeSelector: TypeSelector<Any>) {
        mItemDelegateManager.selector = typeSelector
    }

    override fun getItemViewType(position: Int): Int {
        return if (!useItemViewDelegateManager()) super.getItemViewType(position) else mItemDelegateManager.getItemViewType(
            datas[position],
            position
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val baseTypeItem = mItemDelegateManager.getItemViewDelegate(viewType)
        val layoutId = baseTypeItem.layoutId
        val holder = ViewHolder.createViewHolder(mContext, parent, layoutId)
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    fun onViewHolderCreated(holder: ViewHolder?, itemView: View?) {}
    fun convert(holder: ViewHolder, t: Any,position: Int) {
        mItemDelegateManager.convert(holder, t, position)
    }

    protected fun setListener(parent: ViewGroup?, viewHolder: ViewHolder, viewType: Int) {
        if (!mItemClickEnable) return
        viewHolder.convertView.setOnClickListener { _: View ->
            if (mItemClick != null) {
                val position = viewHolder.adapterPosition
                mItemClick?.invoke(viewHolder, position,getListPosition(position))
                return@setOnClickListener
            }
            if (mBindingItemClick != null) {
                val position = viewHolder.adapterPosition
                mBindingItemClick?.invoke(viewHolder.getBinding<ViewDataBinding>()!!, position,getListPosition(position))
            }
        }
        viewHolder.convertView.setOnLongClickListener { _: View ->
            if (mItemLongClick != null) {
                val position = viewHolder.adapterPosition
                return@setOnLongClickListener mItemLongClick!!.invoke(
                    viewHolder,
                    position,
                    getListPosition(position)
                )
            }
            if (mBindingItemLongClick != null) {
                val position = viewHolder.adapterPosition
                return@setOnLongClickListener mBindingItemLongClick!!.invoke(
                    viewHolder.getBinding<ViewDataBinding>()!!,
                    position,
                    getListPosition(position)
                )
            }
            false
        }
    }

    protected open fun getListPosition(position: Int):Int{
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        convert(holder, datas[position],position)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    fun <T> addItemDelegate(layout: Int, baseTypeItem: BaseTypeItem): BaseMultiItemTypeAdapter {
        mItemDelegateManager.addDelegate(layout, baseTypeItem)
        return this
    }

    fun <T> addItemDelegate(
        viewType: Int,
        layout: Int,
        itemViewDelegate: BaseTypeItem
    ): BaseMultiItemTypeAdapter {
        mItemDelegateManager.addDelegate(viewType, layout, itemViewDelegate)
        return this
    }

    fun useItemViewDelegateManager(): Boolean {
        return mItemDelegateManager.itemViewDelegateCount > 0
    }


}