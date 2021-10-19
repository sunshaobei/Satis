package com.satis.example

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.satis.example.databinding.ActivityMainBinding
import com.satis.sliver.satis

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val list = arrayListOf<String>("Single Type","Multi Type","Header Foot Type","Binding")

        binding.rv.satis(list) {
            singleTypeItem<String>(R.layout.item){i,p,h->
                h.setText(R.id.tv,i)
            }
            itemClick = { _,_,position->
               itemClick(list[position])
            }
        }
    }

   private fun itemClick(text:String){
        when (text){
            "Single Type"->{
                val intent = Intent( this@MainActivity,SingleTypeActivity::class.java)
                startActivity(intent)
            }
            "Multi Type"->{
                val intent = Intent( this@MainActivity,MultiTypeActivity::class.java)
                startActivity(intent)
            }
            "Header Foot Type"->{
                val intent = Intent( this@MainActivity,HeaderAndFootActivity::class.java)
                startActivity(intent)
            }
            "Binding" ->{
                val intent = Intent( this@MainActivity,BindingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}