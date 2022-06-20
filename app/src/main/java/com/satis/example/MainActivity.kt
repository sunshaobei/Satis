package com.satis.example

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.satis.example.databinding.ActivityMainBinding
import com.satis.present.Present
import com.satis.present.PresentDelegate
import com.satis.sliver.sliver

class MainActivity : AppCompatActivity() ,Present by PresentDelegate(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val list = MaineEntry.values().toList()


        binding.rv.sliver {
            datas = list
            item<MaineEntry>{
                layoutId = R.layout.item
                layoutManager = LinearLayoutManager(this@MainActivity)
                itemContent = { i, _, h->
                    h.setText(R.id.tv,i.name)
                }
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