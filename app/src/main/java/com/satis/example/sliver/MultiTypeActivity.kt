package com.satis.example.sliver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
            layoutManager = LinearLayoutManager(this@MultiTypeActivity)
            item<String> {
                layoutId = R.layout.item_multi_type1
                itemContent = { i, p, h ->
                    h.setText(R.id.tv, "type1 -$p")
                }
                typeSelector = {it,p->
                    it.endsWith("1")||
                            it.endsWith("3")||
                    it.endsWith("5")
                }
            }
            item<String> {
                layoutId = R.layout.item_multi_type2
                itemContent = { i, p, h ->
                    h.setText(R.id.tv, "type2 -$p")
                }
                typeSelector = {it,p->
                    it.endsWith("2")||
                            it.endsWith("4")||
                            it.endsWith("6")
                }
            }
            item<String> {
                layoutId = R.layout.item_multi_type3
                itemContent = { i, p, h ->
                    h.setText(R.id.tv, "type3 -$p")
                }
                typeSelector = {it,p->
                    it.endsWith("7")||
                            it.endsWith("9")
                }
            }
            item<String,ItemMultiType4Binding> {
                itemContent = { item, position, binding ->
                    binding.text =  "type4 -$position"
                }
                typeSelector = {it,p->
                    it.endsWith("0")||
                    it.endsWith("8")
                }
            }
        }
    }
}