package com.satis.example.overscroll

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.satis.example.R
import com.satis.example.databinding.ActivityOverScrollTestBinding
import kotlin.random.Random

class OverScrollTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityOverScrollTestBinding>(this,R.layout.activity_over_scroll_test)
        val list = ArrayList<Item>()
        val intExtra = intent.getIntExtra("orientation", 0)
        for (i in 0..20) {
            val item = Item()
            item.text = "binding  -position：$i"
            item.color = Color.argb(255, Random.nextInt(255),Random.nextInt(255),Random.nextInt(255))
            list.add(item)
        }
        binding.items = list
        if (intExtra == 0){
            binding.rv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        }else{
            binding.rv.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        }
    }
}
