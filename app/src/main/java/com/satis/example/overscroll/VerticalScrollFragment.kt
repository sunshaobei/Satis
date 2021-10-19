package com.satis.example.overscroll

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.satis.example.overscroll.VerticalScrollFragment
import com.satist.overscroll.IOverScrollView
import com.satist.overscroll.IOffsetChangeListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.satis.example.R
import kotlin.random.Random

/**
 * @author ziwei huang
 */
class VerticalScrollFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_recyclerview_vertical, container, false)
        initVerticalOverScroll(view)
        return view
    }

    companion object {
        fun initVerticalOverScroll(view: View) {
            val iconHeaderView = view.findViewById<View>(R.id.icon_header)
            val iconFooterView = view.findViewById<View>(R.id.icon_footer)
            val overScrollView: IOverScrollView = view.findViewById(R.id.overscroll_view)
            overScrollView.addOffsetChangeListener(object : IOffsetChangeListener {
                override fun onOffsetChanged(child: View, offset: Int) {
                    if (child.height == 0) {
                        return
                    }
                    val absOffset = Math.abs(offset)
                    val scale = 3 * absOffset * 1f / child.height
                    if (offset >= 0) {
                        iconHeaderView.pivotX = (iconHeaderView.width / 2).toFloat()
                        iconHeaderView.pivotY = 0f
                        iconHeaderView.scaleX = scale
                        iconHeaderView.scaleY = scale
                        iconFooterView.scaleX = 0f
                        iconFooterView.scaleY = 0f
                    } else {
                        iconFooterView.pivotX = (iconFooterView.width / 2).toFloat()
                        iconFooterView.pivotY = iconFooterView.height.toFloat()
                        iconFooterView.scaleX = scale
                        iconFooterView.scaleY = scale
                        iconHeaderView.scaleX = 0f
                        iconHeaderView.scaleY = 0f
                    }
                }
            })
            val recyclerView: RecyclerView = view.findViewById(R.id.overscroll_view)
            recyclerView.layoutManager =
                LinearLayoutManager(recyclerView.context, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                    val factory = LayoutInflater.from(parent.context)
                    val item = factory.inflate(R.layout.item_vertical, parent, false)
                    return object : RecyclerView.ViewHolder(item) {}
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    val textView = holder!!.itemView.findViewById<TextView>(R.id.text)
                    textView.text = "" + (1 + position)
                    val container = holder.itemView.findViewById<View>(R.id.container)
                    val color = Color.argb(255, Random.nextInt(255),
                      Random.nextInt(255),
                       Random.nextInt(255))
                    container.setBackgroundColor(color)
                    container.setOnClickListener {
                        Toast.makeText(
                            holder.itemView.context,
                            "" + (position + 1),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun getItemCount(): Int {
                    return 6
                }
            }
        }
    }
}