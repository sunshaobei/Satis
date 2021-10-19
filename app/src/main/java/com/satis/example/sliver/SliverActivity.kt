package com.satis.example.sliver

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.R
import com.satis.example.databinding.ActivitySliverBinding
import com.satis.sliver.recyclerview.DividerItemDecoration
import com.satis.sliver.satis

class SliverActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sliver)
        val binding =
            DataBindingUtil.setContentView<ActivitySliverBinding>(this, R.layout.activity_sliver)
        val list = arrayListOf<String>("Single Type","Multi Type","Header Foot Type","Binding")

        binding.rv.satis(list) {
            singleTypeItem<String>(R.layout.item){ i, p, h->
                h.setText(R.id.tv,i)
            }
            itemClick = { _,_,position->
                itemClick(list[position])
            }

            divider = {
                orientation = DividerItemDecoration.HORIZONTAL_LIST
                size = 10
                paddingLeft = 20
                color = Color.RED
                paddingRight = 20
            }
        }
    }


    private fun itemClick(text:String){
        when (text){
            "Single Type"->{
                val intent = Intent( this@SliverActivity, SingleTypeActivity::class.java)
                startActivity(intent)
            }
            "Multi Type"->{
                val intent = Intent( this@SliverActivity, MultiTypeActivity::class.java)
                startActivity(intent)
            }
            "Header Foot Type"->{
                val intent = Intent( this@SliverActivity, HeaderAndFootActivity::class.java)
                startActivity(intent)
            }
            "Binding" ->{
                val intent = Intent( this@SliverActivity, BindingActivity::class.java)
                startActivity(intent)
            }
        }
    }

}