package com.satis.sliver.recyclerview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.satis.sliver.item.ItemDelegateManager
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.satis.sliver.BindingItemClick
import com.satis.sliver.BindingItemLongClick
import com.satis.sliver.ItemClick
import com.satis.sliver.ItemLongClick
import com.satis.sliver.item.TypeItem

/**
 * Created by sunsh on 18/5/30.
 */
open class BaseMultiItemTypeAdapter(var mContext: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    var datas: List<Any> = ArrayList()
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

    override fun getItemViewType(position: Int): Int {
        return if (!useItemViewDelegateManager()) super.getItemViewType(position) else mItemDelegateManager.getItemViewType(
            datas[position],
            position
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val typeItem = mItemDelegateManager.getItemViewDelegate(viewType)
        val holder = typeItem.viewHolder
        onViewHolderCreated(holder, holder.convertView)
        setListener(parent, holder, viewType)
        return holder
    }

    fun onViewHolderCreated(holder: ViewHolder?, itemView: View?) {}
    fun convert(holder: ViewHolder, t: Any, position: Int) {
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

    fun <T> addItemDelegate( typeItem: TypeItem<T>): BaseMultiItemTypeAdapter {
        mItemDelegateManager.addDelegate(typeItem)
        return this
    }

    fun <T> addItemDelegate(
        viewType: Int,
        typeItem: TypeItem<T>
    ): BaseMultiItemTypeAdapter {
        mItemDelegateManager.addDelegate(viewType, typeItem)
        return this
    }

    private fun useItemViewDelegateManager(): Boolean {
        return mItemDelegateManager.itemViewDelegateCount > 0
    }


}