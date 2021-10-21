package com.satis.example.sliver

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.satis.example.R
import com.satis.sliver.footView
import com.satis.sliver.headerView
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
                holder.itemView.setBackgroundColor(Color.argb(255, Random().nextInt(255),Random().nextInt(255),Random().nextInt(255)))
            }
            //关健代码
            val textView = TextView(this@HeaderAndFootActivity)
            textView.text = "添加的头部"
            textView.textSize = 100F
            textView.setBackgroundColor(Color.argb(255, Random().nextInt(255),Random().nextInt(255),Random().nextInt(255)))
            header = textView

            divider = {
                size = 15
                color = Color.BLACK
            }

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

    fun addHeader(view: android.view.View) {
        val rv = findViewById<RecyclerView>(R.id.rv)
        val head = TextView(this@HeaderAndFootActivity)
        head.text = "添加头部"
        head.textSize = 50F
        head.setBackgroundColor(Color.argb(255, Random().nextInt(255),Random().nextInt(255),Random().nextInt(255)))
        rv.headerView(head)
    }
    fun addFoot(view: android.view.View) {
        val rv = findViewById<RecyclerView>(R.id.rv)
        val foot = TextView(this@HeaderAndFootActivity)
        foot.text = "添加头部"
        foot.textSize = 50F
        foot.setBackgroundColor(Color.argb(255, Random().nextInt(255),Random().nextInt(255),Random().nextInt(255)))
        rv.footView(foot,true)
    }
}