package com.satis.example.sliver

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.satis.example.R
import com.satis.sliver.recyclerview.wrapper.LoadMoreState
import com.satis.sliver.satis
import java.util.*
import kotlin.collections.ArrayList

class HeaderAndFootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header_and_foot)
        title = "添加头部尾部"
        val list = ArrayList<String>()
        for (i in 0..10){
            list.add("Head AndFoot Type Items -position：$i")
        }
        val rv = findViewById<RecyclerView>(R.id.rv)

        rv.satis(list){
            singleTypeItem<String>(R.layout.item_single_type){ item, position, holder->
                holder.setText(R.id.tv,"Title $item")
            }
            //关健代码
            val textView = TextView(this@HeaderAndFootActivity)
            textView.text = "添加的头部"
            textView.textSize = 100F
            textView.setBackgroundColor(Color.argb(255, Random().nextInt(255),Random().nextInt(255),Random().nextInt(255)))
            textView.setOnClickListener {
//                val head2 = TextView(this@HeaderAndFootActivity)
//                head2.text = "添加头部"
//                head2.textSize = 50F
//                head2.setBackgroundColor(Color.argb(255, Random().nextInt(255),Random().nextInt(255),Random().nextInt(255)))
//                header = head2
//                recyclerView.adapter!!.notifyItemInserted(getHeaderCount())
                list.clear()
                getAdapter().notifyDataSetChanged()
            }
            header = textView

            val textView1 = TextView(this@HeaderAndFootActivity)
            textView1.text = "添加的尾部"
            textView1.textSize = 100F
            textView1.setBackgroundColor(Color.RED)
            footer = textView1


            val tv = TextView(this@HeaderAndFootActivity)
            tv.text = "加载更多"
            tv.textSize = 60f
            loadMoreView(tv){
                Toast.makeText(this@HeaderAndFootActivity, "加载更多", Toast.LENGTH_SHORT)
                    .show()
                setLoadMoreState(LoadMoreState.LoadComplete)
            }


            val empty = TextView(this@HeaderAndFootActivity)
            empty.text = "空内容"
            empty.textSize = 60f
            emptyView = empty
        }
    }
}