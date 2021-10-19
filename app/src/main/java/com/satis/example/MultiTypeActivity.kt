package com.satis.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.satis.example.databinding.ItemMultiType4Binding
import com.satis.sliver.satis

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
        rv.satis(list) {
            selector = { item, position ->
                when {
                    position % 3 == 0 -> {
                        R.layout.item_multi_type1
                    }
                    position%5 == 0 -> {
                        R.layout.item_multi_type2
                    }
                    position%7 == 0 -> {
                        R.layout.item_multi_type3
                    }
                    else -> R.layout.item_multi_type4
                }
            }
            multiTypeItems<String>(R.layout.item_multi_type1) { i, p, h ->
                h.setText(R.id.tv, "type1 -$p")
            }
            multiTypeItems<String>(R.layout.item_multi_type2) { i, p, h ->
                h.setText(R.id.tv, "type2 -$p")
            }
            multiTypeItems<String>(R.layout.item_multi_type3) { i, p, h ->
                h.setText(R.id.tv, "type3 -$p")
            }
            multiTypeItemsBinding<String,ItemMultiType4Binding>(R.layout.item_multi_type4) { i, p,b ->
                b.text  = "type4 -$p"
            }
        }
    }
}