package com.satis.example.overscroll

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.satis.example.R
import com.satis.example.databinding.ActivityCoordinatorBinding
import com.satis.overscroll.addOverScrollOffsetChangeListener
import com.satis.overscroll.api.RefreshImp
import com.satis.overscroll.refreshComplete
import com.satis.overscroll.setOnRefreshListener
import kotlin.random.Random

class CoordinatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityCoordinatorBinding>(
            this,
            R.layout.activity_coordinator
        )
        val list = ArrayList<Item>()
        for (i in 0..20) {
            val item = Item()
            item.text = "binding  -position：$i"
            item.color =
                Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
            list.add(item)
        }
        binding.items = list
        binding.rv.setOnRefreshListener(Refresh(binding.tv)) {
            Handler().postDelayed({
                binding.rv.refreshComplete()
            }, 2000)
        }
    }

    class Refresh( mRefreshView: View) : RefreshImp(mRefreshView) {
        override fun onStateChange(state: Int) {
            when (state) {
                RefreshImp.REFRESHING -> {
                    (mRefreshView as TextView).text = "正在刷新"
                }
                RefreshImp.PULL_DOWN -> {
                    (mRefreshView as TextView).text = "下拉刷新"
                }
                RefreshImp.PULL_DOWN_OVER -> {
                    (mRefreshView as TextView).text = "松开刷新"
                }
                RefreshImp.RELEASE -> {
                    (mRefreshView as TextView).text = "刷新完成"
                }
            }
        }

        override fun updateOffset(offset: Int) {
            mRefreshView.translationY = offset.toFloat()
        }

        override fun getRefreshHeight(): Int {
            return mRefreshView.height
        }

    }
}