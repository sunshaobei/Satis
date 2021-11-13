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
        val list = MaineEntry.values().toList()


        binding.rv.satis(list) {
            singleTypeItem<MaineEntry>(R.layout.item){ i, _, h->
                h.setText(R.id.tv,i.name)
            }
            itemClick = { _,_,position->
               itemClick(list[position])
            }
            divider = {
                size = 10
                paddingLeft = 20
                color = Color.RED
                paddingRight = 20
            }
        }
    }

   private fun itemClick(maineEntry: MaineEntry){
       val intent = Intent( this@MainActivity, maineEntry.aClass)
       startActivity(intent)
    }
}