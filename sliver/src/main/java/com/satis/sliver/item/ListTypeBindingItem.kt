package com.satis.sliver.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.satis.sliver.TypeSelector
import com.satis.core.AppContext.context
import com.satis.sliver.recyclerview.ViewHolder
import java.lang.Exception
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

abstract class ListTypeBindingItem<T,K:ViewDataBinding>(val parent:RecyclerView, override var selector: TypeSelector<T>? = null):
    TypeItem<T> {
    override var viewHolder: ViewHolder
    get() {
        return ViewHolder.createViewHolder(initBinding()!!.root)
    }
    set(value) {}
    var itemContent: ((item: T, position: Int,K) -> Unit) ? = null


    @Suppress("UNCHECKED_CAST")
    private fun initBinding():ViewDataBinding? {
        val type = this.javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = type.actualTypeArguments
        // init viewBinding
        val dataBindingClass = actualTypeArguments[1] as Class<K>
        var method: Method?
        try {
            method = dataBindingClass.getMethod("inflate", LayoutInflater::class.java,ViewGroup::class.java,Boolean::class.java)
            method.isAccessible = true
            val layoutInflater = LayoutInflater.from(context)
            return (method.invoke(dataBindingClass, layoutInflater,parent,false) as K)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}