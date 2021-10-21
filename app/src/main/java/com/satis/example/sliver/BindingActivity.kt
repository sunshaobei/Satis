package com.satis.example.sliver

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.R
import com.satis.example.databinding.ActivityBindingBinding
import com.satis.example.overscroll.Item
import kotlin.random.Random

class BindingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding =  DataBindingUtil.setContentView<ActivityBindingBinding>(this,
           R.layout.activity_binding
       )
        title = "xml binding"
        val list = ArrayList<Item>()
        for (i in 0..100) {
            val item = Item()
            item.text = "binding  -position：$i"
            item.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
            list.add(item)
        }
        binding.items = list
    }
}