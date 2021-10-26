package com.satis.example.overscroll

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.R
import com.satis.example.databinding.ActivityCoordinatorBinding
import kotlin.random.Random

class CoordinatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCoordinatorBinding>(this,R.layout.activity_coordinator)
        val list = ArrayList<Item>()
        for (i in 0..20) {
            val item = Item()
            item.text = "binding  -position：$i"
            item.color = Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
            list.add(item)
        }
        binding.items = list
    }
}