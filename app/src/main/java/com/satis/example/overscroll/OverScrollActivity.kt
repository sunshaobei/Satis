package com.satis.example.overscroll

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.*
import com.satis.example.databinding.ActivityOverScrollBinding
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
//            divider = {
//                size = 10
//                paddingLeft = 20
//                color = Color.RED
//                paddingRight = 20
//            }
        }
    }


    private fun itemClick(text: String) {
        when (text) {
            "HORIZONTAL_SCROLL" -> {
            }
            "VERTICAL" -> {
            }
            "NESTED_SCROLL" -> {
            }
            "TEST" -> {
            }
        }
    }
}