package com.satis.sliver.recyclerview.wrapper

import android.content.Context
import android.view.View
import com.satis.sliver.recyclerview.ViewHolder.Companion.createViewHolder
import com.satis.sliver.utils.WrapperUtils.onAttachedToRecyclerView
import com.satis.sliver.utils.WrapperUtils.setFullSpan
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.satis.sliver.LoadMoreCallback
import com.satis.sliver.recyclerview.ViewHolder

/**
 * Created by sunsh on 18/5/30.
 */
open class LoadMoreWrapper(mContext: Context, datas: List<Any>) : HeaderAndFootWrapper(mContext, datas) {
    private var mLoadMoreView: View? = null
    private var mLoadMoreLayoutId = 0
    var mLoadMoreCallback:LoadMoreCallback ? = null
    var mLoadMoreState = LoadMoreState.LoadComplete
    private val  mLoadMoreStateManager = LoadMoreStateManager(this)

    private fun loadMoreEnable(): Boolean {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0
    }

    private fun isShowLoadMore(position: Int): Boolean {
        return loadMoreEnable() && position >= super.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowLoadMore(position)) {
            ITEM_TYPE_LOAD_MORE
        } else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            val holder: ViewHolder = if (mLoadMoreView != null) {
                createViewHolder(
                    parent.context,
                    mLoadMoreView!!
                )
            } else {
                createViewHolder(
                    parent.context,
                    parent,
                    mLoadMoreLayoutId
                )
            }
            holder.setIsRecyclable(false)
            return holder
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        onAttachedToRecyclerView(recyclerView) { layoutManager: GridLayoutManager, oldLookup: SpanSizeLookup?, position: Int ->
            if (isShowLoadMore(position)) {
                return@onAttachedToRecyclerView layoutManager.spanCount
            }
            if (oldLookup != null) {
                return@onAttachedToRecyclerView oldLookup.getSpanSize(position)
            }
            1
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        if (isShowLoadMore(holder.layoutPosition)) {
            if (mLoadMoreState == LoadMoreState.LoadComplete){
                mLoadMoreState = LoadMoreState.Loading
                mLoadMoreCallback?.invoke(mLoadMoreStateManager)
            }
            setFullSpan(holder)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (loadMoreEnable()) 1 else 0
    }

    fun setLoadMoreView(loadMoreView: View?) {
        mLoadMoreView = loadMoreView
        mLoadMoreLayoutId = 0
    }

    fun setLoadMoreView(layoutId: Int) {
        mLoadMoreLayoutId = layoutId
        mLoadMoreView = null
    }

    companion object {
        const val ITEM_TYPE_LOAD_MORE = Int.MAX_VALUE - 2
    }
}