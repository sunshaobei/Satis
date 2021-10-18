package com.satis.scrollview.recyclerview.wrapper

import android.content.Context
import android.view.View
import com.satis.scrollview.recyclerview.ViewHolder.Companion.createViewHolder
import androidx.recyclerview.widget.RecyclerView
import androidx.collection.SparseArrayCompat
import android.view.ViewGroup
import com.satis.scrollview.recyclerview.ViewHolder
import com.satis.scrollview.utils.WrapperUtils

/**
 * Created by sunsh on 18/5/30.
 */
open class HeaderAndFootWrapper(context: Context, datas: List<Any>) : EmptyWrapper(context,datas) {
    private val mHeaderViews = SparseArrayCompat<View>()
    private val mFootViews = SparseArrayCompat<View>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mHeaderViews[viewType] != null || mFootViews[viewType] != null) {
            var o: View? = null
            o = mHeaderViews[viewType]
            if (o == null) {
                o = mFootViews[viewType]
            }
            var holder: ViewHolder? = null
            if (o is View) {
                holder = createViewHolder(parent.context, (o as View?)!!)
            }
            return holder!!
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - realItemCount)
        }
        return super.getItemViewType(position - headersCount)
    }

    override fun getListPosition(position:Int):Int{
        return position - headersCount
    }

    private val realItemCount: Int
        private get() = super.getItemCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        } else if (isFooterViewPos(position)) {
            return
        }
        super.onBindViewHolder(holder, getListPosition(position))
    }

    override fun getItemCount(): Int {
        return headersCount + footersCount + realItemCount
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView( recyclerView) { layoutManager, oldLookup, position ->
            val viewType = getItemViewType(position)
            if (mHeaderViews[viewType] != null) {
                return@onAttachedToRecyclerView layoutManager.spanCount
            } else if (mFootViews[viewType] != null) {
                return@onAttachedToRecyclerView layoutManager.spanCount
            }else if (isDataEmpty()){
                return@onAttachedToRecyclerView layoutManager.spanCount
            }
            if (oldLookup != null) return@onAttachedToRecyclerView oldLookup.getSpanSize(position)
            1
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + realItemCount
    }

    fun addHeaderView(view: View?) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view)
    }

    fun removeHeaderView(v: View?) {
        val i = mHeaderViews.indexOfValue(v)
        if (i >= 0) {
            notifyItemRemoved(i)
            mHeaderViews.remove(i)
        }
    }


    fun addFootView(view: View?) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view)
    }

    fun removeFootView(view: View?) {
        val i = mFootViews.indexOfValue(view)
        if (i >= 0) {
            notifyItemRemoved(i)
            mFootViews.remove(i)
        }
    }

    private fun isDataEmpty(): Boolean {
        return (mEmptyView != null || mEmptyLayoutId != 0) && super.getItemCount() == 0
    }

    private val mEmptyView: View? = null
    private val mEmptyLayoutId = 0

    val headersCount: Int
        get() = mHeaderViews.size()
    val footersCount: Int
        get() = mFootViews.size()

    companion object {
        private const val BASE_ITEM_TYPE_HEADER = 100000
        private const val ITEM_TYPE_EMPTY = Int.MAX_VALUE - 1
        private const val BASE_ITEM_TYPE_FOOTER = 200000
    }
}