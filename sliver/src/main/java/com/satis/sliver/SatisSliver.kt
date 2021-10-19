package com.satis.sliver

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.satis.sliver.item.ListTypeBindingItem
import com.satis.sliver.item.ListTypeItem
import com.satis.sliver.recyclerview.ViewHolder
import com.satis.sliver.recyclerview.wrapper.LoadMoreState

class SatisSliver(val recyclerView: RecyclerView, datas: List<Any>) {
    private var adapter = SatisAdapter(recyclerView.context, datas)
    var layoutManager: RecyclerView.LayoutManager? = null
    var divider: RecyclerView.ItemDecoration? = null
    var header: View? = null
    set(value) {
        adapter.addHeaderView(value)
        field = value
    }
    var footer: View? = null
        set(value) {
            adapter.addFootView(value)
            field = value
        }

    var itemClick: ItemClick? = null
        set(value) {
            adapter.mItemClick = value
            field = value
        }

    var emptyView: View? = null
        set(value) {
            adapter.setEmptyView(value)
            field = value
        }
    var emptyLayoutId: Int = 0
        set(value) {
            adapter.setEmptyView(value)
            field = value
        }

    var loadMoreView: Int = 0
        set(value) {
            adapter.setLoadMoreView(value)
            field = value
        }

    var loadMoreLayoutId: Int = 0
        set(value) {
            adapter.setLoadMoreView(value)
            field = value
        }

    var LoadMoreCallback:LoadMoreCallback? = null
        set(value) {
            adapter.mLoadMoreCallback = value
            field = value
        }

    fun loadMoreView(view: View,block:LoadMoreCallback){
        adapter.setLoadMoreView(view)
        adapter.mLoadMoreCallback = block
    }
    fun loadMoreView(layoutId: Int,block:LoadMoreCallback){
        adapter.setLoadMoreView(layoutId)
        adapter.mLoadMoreCallback = block
    }

    /**
     * 多类型判断选择器
     */
    var selector:TypeSelector<Any>? = null
        set(value) {
            value as TypeSelector<Any>
            adapter.setTypeSelector(value)
            field = value
        }

    fun getAdapter(): SatisAdapter {
        return adapter
    }

    /**
     * 多类型判断选择器
     */
    fun <T> selector(
        sel: TypeSelector<T>,
    ) {
        sel as TypeSelector<Any>
        adapter.setTypeSelector(sel)
    }

    /**
     *  T指的是 集合参数类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    fun <T> singleTypeItem(
        @LayoutRes layoutId: Int,
        itemContent: (item: T, position: Int, holder: ViewHolder) -> Unit
    ) {
        var defaultTypeItem = ListTypeItem<T>(layoutId)
        defaultTypeItem.itemContent = itemContent
        adapter.addItemDelegate<T>(layoutId, defaultTypeItem)
    }

    /**
     *  T, K 分别指的是 集合参数类型及 viewbinding 类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    fun <T, K : ViewDataBinding> singleTypeItemBinding(
        @LayoutRes layoutId: Int,
        itemContent: (item: T, position: Int, binding: K) -> Unit
    ) {
        var listTypeBindingItem = ListTypeBindingItem<T,K>(layoutId)
        listTypeBindingItem.itemContent = itemContent
        adapter.addItemDelegate<T>(layoutId, listTypeBindingItem)
    }


    /**
     *  T指的是 集合参数类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    fun <T> multiTypeItems(
        @LayoutRes layoutId: Int,
        itemContent: (item: T, position: Int, ViewHolder: ViewHolder) -> Unit,
    ) {
        var multiTypeItem = ListTypeItem<T>(layoutId)
        multiTypeItem.itemContent = itemContent
        adapter.addItemDelegate<T>(layoutId, multiTypeItem)
    }

    /**
     *  T指的是 集合参数类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    fun multiTypeItemsWithMultiModel(
        @LayoutRes layoutId: Int,
        itemContent: (item: Any, position: Int, holder: ViewHolder) -> Unit
    ) {
        var multiTypeItem = ListTypeItem<Any>(layoutId)
        multiTypeItem.itemContent = itemContent
        adapter.addItemDelegate<Any>(layoutId, multiTypeItem)
    }

    /**
     *  T, K 分别指的是 集合参数类型及 viewbinding 类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    fun <T, K : ViewDataBinding> multiTypeItemsBinding(
        @LayoutRes layoutId: Int,
        itemContent: (item: T, position: Int, binding: K) -> Unit
    ) {
        var listTypeBindingItem = ListTypeBindingItem<T, K>(layoutId)
        listTypeBindingItem.itemContent = itemContent
        adapter.addItemDelegate<T>(layoutId, listTypeBindingItem)
    }

    /**
     *  T, K 分别指的是 集合参数类型及 viewbinding 类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    fun <K : ViewDataBinding> multiTypeItemsWithMultiModelBinding(
        @LayoutRes layoutId: Int,
        itemContent: (item: Any, position: Int, binding: K) -> Unit
    ) {
        var listTypeBindingItem = ListTypeBindingItem<Any, K>(layoutId)
        listTypeBindingItem.itemContent = itemContent
        adapter.addItemDelegate<Any>(layoutId, listTypeBindingItem)
    }

    fun setLoadMoreState(loadMoreState: LoadMoreState){
        adapter.mLoadMoreState = loadMoreState
    }

    fun getHeaderCount():Int{
        return adapter.headersCount
    }
}

fun RecyclerView.satis(datas: List<Any>, block: SatisSliver.() -> Unit):SatisSliver {
    val sliver = SatisSliver(this, datas)
    sliver.block()
    //设置layoutManager
    this.layoutManager = sliver.layoutManager ?: layoutManager ?: LinearLayoutManager(this.context)
    //添加分割线
    sliver.divider?.let {
        addItemDecoration(it)
    }
    adapter = sliver.getAdapter()
    return sliver
}