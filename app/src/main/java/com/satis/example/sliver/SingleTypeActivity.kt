package com.satis.example.sliver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.satis.example.R
import com.satis.sliver.satis

class SingleTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_type)
        title = "单一type"
        val list = ArrayList<String>()
        for (i in 0..100) {
            list.add("Single Type Items -position：$i")
        }
        val rv = findViewById<RecyclerView>(R.id.rv)

        rv.satis(list) {
            singleTypeItem<String>(R.layout.item_single_type) { item, position, holder ->
                holder.setText(R.id.tv, "Title $item")
            }
            itemClick = { _, p1, p2 ->
                Toast.makeText(this@SingleTypeActivity, "点击 position -$p2", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}