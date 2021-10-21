package com.satis.example.overscroll

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.satis.example.R
import com.satis.example.databinding.ActivityNestedScrollBinding
import java.util.*

class NestedScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       val binding =  DataBindingUtil.setContentView<ActivityNestedScrollBinding>(this,R.layout.activity_nested_scroll)
        for (i in 0..10){
            val textView = TextView(this)
            textView.layoutParams =  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400)
            textView.textSize = 20f
            textView.setBackgroundColor(
                Color.argb(255, Random().nextInt(255),
                Random().nextInt(255),
                Random().nextInt(255)))
            textView.gravity= Gravity.CENTER
            textView.text = "position  $i"
            binding.ll.addView(textView)
        }
    }
}