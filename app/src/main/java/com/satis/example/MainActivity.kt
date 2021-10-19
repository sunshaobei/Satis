package com.satis.example

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.databinding.ActivityMainBinding
import com.satis.example.overscroll.OverScrollActivity
import com.satis.example.sliver.SliverActivity
import com.satis.sliver.recyclerview.DividerItemDecoration
import com.satis.sliver.satis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val list = arrayListOf<String>("SLIVER","OVERSCROLL")

        binding.rv.satis(list) {
            singleTypeItem<String>(R.layout.item){i,p,h->
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
            "SLIVER"->{
                val intent = Intent( this@MainActivity, SliverActivity::class.java)
                startActivity(intent)
            }
            "OVERSCROLL"->{
                val intent = Intent( this@MainActivity, OverScrollActivity::class.java)
                startActivity(intent)
            }
        }
    }
}