package com.satis.sliver

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.satis.sliver.item.ListTypeBindingItem
import com.satis.sliver.item.ListTypeItem
import com.satis.sliver.recyclerview.DividerItemDecoration
import com.satis.sliver.recyclerview.ViewHolder
import com.satis.sliver.recyclerview.wrapper.LoadMoreState
import com.satis.sliver.recyclerview.wrapper.LoadMoreStateManager
import java.lang.IllegalArgumentException

class SatisSliver(val recyclerView: RecyclerView) {
    val adapter = SatisAdapter(recyclerView.context)
    var datas: List<Any>? = null
        set(value) {
            adapter.datas = value ?: ArrayList()
            field = value
        }
    var layoutManager: RecyclerView.LayoutManager? = null
        set(value) {
            recyclerView.layoutManager = value
            field = value
        }
    var divider: (DividerItemDecoration.() -> Unit)? = null
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

    //暂不开放
    private var emptyLayoutId: Int = 0
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

    var LoadMoreCallback: LoadMoreCallback? = null
        set(value) {
            adapter.mLoadMoreCallback = value
            field = value
        }

    fun loadMoreView(view: View, block: LoadMoreCallback) {
        adapter.setLoadMoreView(view)
        adapter.mLoadMoreCallback = block
    }

    fun loadMoreView(layoutId: Int, block: LoadMoreCallback) {
        adapter.setLoadMoreView(layoutId)
        adapter.mLoadMoreCallback = block
    }

    /**
     *  T指的是 集合参数类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    fun <T> item(
       layoutItemBox: LayoutItemBox<T>.()->Unit
    ) {
        LayoutItemBox<T>().apply(layoutItemBox).apply{
            val item =  ListTypeItem<T>(layoutId, typeSelector,recyclerView)
            item.itemContent = itemContent
            adapter.addItemDelegate(item)
        }
    }

    /**
     *  T, K 分别指的是 集合参数类型及 viewbinding 类型
     *  @param items 数据集合
     *  @param layoutId 单type 布局结构
     *  @param itemContent recyclerView convertView
     */
    @JvmName("bindingItem")
    inline fun <reified T, reified K : ViewDataBinding> item(
       bindingItemBox: BindingItemBox<T, K>.()->Unit
    ) {
        BindingItemBox<T,K>().apply(bindingItemBox).apply {
            val listTypeBindingItem: ListTypeBindingItem<T, K> = object :
                ListTypeBindingItem<T, K>(recyclerView,typeSelector){}
            listTypeBindingItem.itemContent = itemContent
            adapter.addItemDelegate(listTypeBindingItem)
        }
    }

    fun setLoadMoreState(loadMoreState: LoadMoreState) {
        adapter.mLoadMoreState = loadMoreState
    }

    fun getHeaderCount(): Int {
        return adapter.headersCount
    }
}

class LayoutItemBox<T> {
    var layoutId = 0
        get() {
            if (field>0){
                return field
            }
            throw IllegalArgumentException("请设置 layoutId ")
        }
    var typeSelector: TypeSelector<T>? = null
    var itemContent: ((item: T, position: Int, holder: ViewHolder) -> Unit)? = null
}

class BindingItemBox<T,K:ViewDataBinding> {
    var typeSelector: TypeSelector<T>? = null
    var itemContent: ((item: T, position: Int, binding: K) -> Unit)? = null
}


fun RecyclerView.sliver(block: SatisSliver.() -> Unit): SatisSliver {
    val sliver = SatisSliver(this)
    sliver.block()
    //设置layoutManager
    if (sliver.layoutManager != null){
        layoutManager = sliver.layoutManager
    }
    //添加分割线
    sliver.divider?.let {
        addItemDecoration(DividerItemDecoration(this).apply { it() })
    }
    adapter = sliver.adapter
    return sliver
}


fun RecyclerView.loadMore(view: View, loadMoreCallback: LoadMoreCallback) {
    if (adapter is SatisAdapter) {
        (adapter as SatisAdapter).setLoadMoreView(view)
        (adapter as SatisAdapter).mLoadMoreCallback = loadMoreCallback
    }
}

fun RecyclerView.loadMore(layoutId: Int, loadMoreCallback: LoadMoreCallback) {
    if (adapter is SatisAdapter) {
        (adapter as SatisAdapter).setLoadMoreView(layoutId)
        (adapter as SatisAdapter).mLoadMoreCallback = loadMoreCallback
    }
}

fun RecyclerView.empty(view: View) {
    if (adapter is SatisAdapter) {
        (adapter as SatisAdapter).setEmptyView(view)
    }
}


fun RecyclerView.divider(divider: DividerItemDecoration.() -> Unit) {
    addItemDecoration(DividerItemDecoration(this).apply { divider() })
}

fun RecyclerView.headerView(view: View, insertAnimate: Boolean = false) {
    if (adapter is SatisAdapter) {
        (adapter as SatisAdapter).addHeaderView(view)
        if (insertAnimate) {
            (adapter as SatisAdapter).notifyItemInserted((adapter as SatisAdapter).headersCount)
        } else {
            (adapter as SatisAdapter).notifyDataSetChanged()
        }
    }
}

fun RecyclerView.footView(view: View, insertAnimate: Boolean = false) {
    if (adapter is SatisAdapter) {
        (adapter as SatisAdapter).addFootView(view)
        if (insertAnimate) {
            val adapter1 = (adapter as SatisAdapter)
            var itemCount = adapter1.itemCount
            val loadMoreEnable = adapter1.loadMoreEnable()
            if (loadMoreEnable) {
                itemCount -= 1
            }
            (adapter as SatisAdapter).notifyItemInserted(itemCount)
        } else {
            (adapter as SatisAdapter).notifyDataSetChanged()
        }
    }
}

typealias LoadMoreCallback = LoadMoreStateManager.()->Unit
/**
 *  第二、三参数分别为：在recyclerview的实际位置 与 List 数据索引位置
 */
typealias ItemClick = (holder: ViewHolder, realPosition:Int, listPosition:Int)->Unit
typealias BindingItemClick = (ViewDataBinding,Int,Int)->Unit
typealias ItemLongClick = (ViewHolder, Int, Int)->Boolean
typealias BindingItemLongClick = (ViewDataBinding,Int,Int)->Boolean
typealias SpanSizeCallback = (GridLayoutManager, GridLayoutManager.SpanSizeLookup?, Int) -> Int
typealias TypeSelector<T> =  (T) -> Boolean