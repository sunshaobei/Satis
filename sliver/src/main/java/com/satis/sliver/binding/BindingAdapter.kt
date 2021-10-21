package com.satis.sliver.binding

import androidx.databinding.BindingAdapter
import com.satis.sliver.satis
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding
import com.satis.sliver.recyclerview.ViewHolder
import java.lang.Exception
import java.util.*

object BindingAdapter {
    /**
     * 定义多个属性
     *  暂时不支持多type 展示 ，多type展示建议使用[RecyclerView.satis]
     * "selector","multiItem1", "multiItem2", "multiItem3", "multiItem4", "multiItem5", "multiItem6", "multiItem7", "multiItem8", "multiItem9", "multiItem10", "multiItem11", "multiItem12", "multiItem13", "multiItem14", "multiItem15", "multiItem6", "multiItem17", "multiItem18", "multiItem19", "multiItem20"
     */
    @JvmStatic
    @BindingAdapter(value = ["items", "singleTypeItem", "bindingBR"])
    fun recyclerView(view: RecyclerView, items: List<Any>, singleTypeItem: Int, bindingBR: String) {
        view.satis(items) {
            singleTypeItem<Any>(singleTypeItem) { o: Any?, integer: Int?, viewHolder: ViewHolder ->
                try {
                    val forName = Class.forName(view.context.packageName + ".BR")
                    val declaredField = forName.getDeclaredField(bindingBR)
                    declaredField.isAccessible = true
                    viewHolder.getBinding<ViewDataBinding>()!!
                        .setVariable(declaredField[null] as Int, o)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}