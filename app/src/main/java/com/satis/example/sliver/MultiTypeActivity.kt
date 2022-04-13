package com.satis.example.sliver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.satis.example.R
import com.satis.example.databinding.ItemMultiType4Binding
import com.satis.sliver.sliver

class MultiTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_type)

        title = "多type"
        val list = ArrayList<String>()
        for (i in 0..100) {
            list.add("Multi Type Items -position：$i")
        }
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.sliver {
            datas = list
            item<String> {
                layoutId = R.layout.item_multi_type1
                itemContent = { i, p, h ->
                    h.setText(R.id.tv, "type1 -$p")
                }
            }
            item<String> {
                layoutId = R.layout.item_multi_type2
                itemContent = { i, p, h ->
                    h.setText(R.id.tv, "type2 -$p")
                }
            }
            item<String> {
                layoutId = R.layout.item_multi_type3
                itemContent = { i, p, h ->
                    h.setText(R.id.tv, "type3 -$p")
                }
            }
            item<String,ItemMultiType4Binding> {
                itemContent = { i, p, h ->
                    h.text =  "type4 -$p"
                }
            }
        }
    }
}