package com.satis.scrollview.recyclerview.wrapper

import android.content.Context
import android.view.View
import com.satis.scrollview.recyclerview.ViewHolder.Companion.createViewHolder
import com.satis.scrollview.utils.WrapperUtils.onAttachedToRecyclerView
import com.satis.scrollview.utils.WrapperUtils.setFullSpan
import com.satis.scrollview.recyclerview.BaseMultiItemTypeAdapter
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.satis.scrollview.recyclerview.ViewHolder

/**
 * Created by sunsh on 18/5/30.
 */
open class EmptyWrapper(mContext: Context, datas: List<Any>) :
    BaseMultiItemTypeAdapter(mContext, datas) {
    private var mEmptyView: View? = null
    private var mEmptyLayoutId = 0
    private val isEmpty: Boolean
        private get() = (mEmptyView != null || mEmptyLayoutId != 0) && super.getItemCount() == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (isEmpty) {
            val holder: ViewHolder = if (mEmptyView != null) {
                createViewHolder(parent.context, mEmptyView!!)
            } else {
                createViewHolder(
                    parent.context,
                    parent,
                    mEmptyLayoutId
                )
            }
            return holder
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        onAttachedToRecyclerView(recyclerView) { gridLayoutManager: GridLayoutManager, spanSizeLookup: SpanSizeLookup?, integer: Int? ->
            if (isEmpty) {
                return@onAttachedToRecyclerView gridLayoutManager.spanCount
            }
            if (spanSizeLookup != null) {
                return@onAttachedToRecyclerView spanSizeLookup.getSpanSize(integer!!)
            }
            1
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        if (isEmpty) {
            setFullSpan(holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEmpty) {
            ITEM_TYPE_EMPTY
        } else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isEmpty) {
            return
        }
        super.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return if (isEmpty) 1 else super.getItemCount()
    }

    fun setEmptyView(emptyView: View?) {
        mEmptyView = emptyView
        mEmptyLayoutId = 0
    }

    fun setEmptyView(layoutId: Int) {
        mEmptyLayoutId = layoutId
        mEmptyView = null
    }

    companion object {
        const val ITEM_TYPE_EMPTY = Int.MAX_VALUE - 1
    }
}