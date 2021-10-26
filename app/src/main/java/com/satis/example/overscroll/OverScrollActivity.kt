package com.satis.example.overscroll

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.*
import com.satis.example.databinding.ActivityOverScrollBinding
import com.satis.example.sliver.SingleTypeActivity
import com.satis.sliver.satis

class OverScrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_over_scroll)
        title = "OverScroll"
        val binding =
            DataBindingUtil.setContentView<ActivityOverScrollBinding>(
                this,
                R.layout.activity_over_scroll
            )
        val list = arrayListOf<String>("HORIZONTAL_SCROLL", "VERTICAL", "NESTED_SCROLL", "TEST")

        binding.rv.satis(list) {
            singleTypeItem<String>(R.layout.item) { i, p, h ->
                h.setText(R.id.tv, i)
            }
            itemClick = { _, _, position ->
                itemClick(list[position])
            }
        }
    }


    private fun itemClick(text: String) {
        when (text) {
            "HORIZONTAL_SCROLL" -> {
                val intent = Intent( this@OverScrollActivity, OverScrollTestActivity::class.java)
                intent.putExtra("orientation",1)
                startActivity(intent)
            }
            "VERTICAL" -> {
                val intent = Intent( this@OverScrollActivity, OverScrollTestActivity::class.java)
                intent.putExtra("orientation",0)
                startActivity(intent)
            }
            "NESTED_SCROLL" -> {
                val intent = Intent( this@OverScrollActivity, NestedScrollActivity::class.java)
                startActivity(intent)
            }
            "TEST" -> {
                val intent = Intent( this@OverScrollActivity, CoordinatorActivity::class.java)
                startActivity(intent)
            }
        }
    }
}