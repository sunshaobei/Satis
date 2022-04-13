package com.satis.example.sliver

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.satis.example.R
import com.satis.sliver.sliver
import java.util.*
import kotlin.collections.ArrayList

class SingleTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_type)
        title = "单一type"
        val list = ArrayList<String>()
        for (i in 0..21) {
            list.add("Single Type Items -position：$i")
        }
        val rv = findViewById<RecyclerView>(R.id.rv)

        rv.sliver {
            datas = list
            layoutManager = LinearLayoutManager(this@SingleTypeActivity)
            item<String> {
                layoutId = R.layout.item_single_type
                itemContent = { item, position, holder ->
                    holder.setText(R.id.tv, "Title $item")
                    holder.itemView.setBackgroundColor(
                        Color.argb(
                            255, Random().nextInt(255),
                            Random().nextInt(255),
                            Random().nextInt(255)
                        )
                    )
                }
            }
            itemClick = { _, p1, p2 ->
                Toast.makeText(this@SingleTypeActivity, "点击 position -$p2", Toast.LENGTH_SHORT)
                    .show()
            }
            divider = {
                size = 10
                color = Color.RED
            }
        }
    }

    fun change(view: android.view.View) {
        val rv = findViewById<RecyclerView>(R.id.rv)
        if (rv.layoutManager is GridLayoutManager) {
            rv.layoutManager = LinearLayoutManager(this@SingleTypeActivity)
        } else {
            rv.layoutManager = GridLayoutManager(this@SingleTypeActivity, 2)
        }
    }
}