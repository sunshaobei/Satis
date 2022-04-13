package com.satis.sliver.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding
import com.satis.sliver.recyclerview.ViewHolder
import com.satis.sliver.sliver
import java.lang.Exception
object BindingAdapter {
    /**
     * 定义多个属性
     *  暂时不支持多type 展示 ，多type展示建议使用[RecyclerView.sliver]
     */
    @JvmStatic
    @BindingAdapter(value = ["datas","layoutId","brStr"])
    fun recyclerView(
        view: RecyclerView,
        datas: List<Any>,
        layoutId: Int,
        brStr: String,
    ) {
        view.sliver {
            this.datas = datas
            item<Any> {
                this.layoutId = layoutId
                itemContent = { o: Any?, _: Int?, viewHolder: ViewHolder ->
                    try {
                        val forName = Class.forName(view.context.packageName + ".BR")
                        val declaredField = forName.getDeclaredField(brStr)
                        declaredField.isAccessible = true
                        viewHolder.getBinding<ViewDataBinding>()!!.setVariable(declaredField[null] as Int, o)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}