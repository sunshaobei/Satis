package com.satis.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.databinding.ActivityBindingBinding

class BindingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding =  DataBindingUtil.setContentView<ActivityBindingBinding>(this,R.layout.activity_binding)
        title = "xml binding"
        val list = ArrayList<String>()
        for (i in 0..100) {
            list.add("binding  -position：$i")
        }
        binding.items = list
    }
}