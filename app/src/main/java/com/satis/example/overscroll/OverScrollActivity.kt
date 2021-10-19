package com.satis.example.overscroll

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.satis.example.*
import com.satis.example.databinding.ActivityOverScrollBinding
import com.satis.example.sliver.SingleTypeActivity
import com.satis.sliver.recyclerview.DividerItemDecoration
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
            divider = {
//                orientation = DividerItemDecoration.HORIZONTAL_LIST
                size = 10
                paddingLeft = 20
                color = Color.RED
                paddingRight = 20
            }
        }
    }

    private val horizontalScrollFragment = HorizontalScrollFragment()
    private val verticalScrollFragment = VerticalScrollFragment()
    private val nestedScrollFragment = NestedScrollFragment()
    private val testFragment = TestFragment()

    private fun itemClick(text: String) {
        when (text) {
            "HORIZONTAL_SCROLL" -> {
                if (horizontalScrollFragment.isAdded) {
                    supportFragmentManager.beginTransaction().show(horizontalScrollFragment)
                        .commitAllowingStateLoss()
                    return
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, horizontalScrollFragment)
                    .commitAllowingStateLoss()
            }
            "VERTICAL" -> {
                if (verticalScrollFragment.isAdded) {
                    supportFragmentManager.beginTransaction().show(verticalScrollFragment)
                        .commitAllowingStateLoss()
                    return
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, verticalScrollFragment)
                    .commitAllowingStateLoss()
            }
            "NESTED_SCROLL" -> {
                if (nestedScrollFragment.isAdded) {
                    supportFragmentManager.beginTransaction().show(nestedScrollFragment)
                        .commitAllowingStateLoss()
                    return
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, nestedScrollFragment)
                    .commitAllowingStateLoss()
            }
            "TEST" -> {
                if (testFragment.isAdded) {
                    supportFragmentManager.beginTransaction().show(testFragment)
                        .commitAllowingStateLoss()
                    return
                }
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, testFragment)
                    .commitAllowingStateLoss()
            }
        }
    }

    override fun onBackPressed() {
        var fragment: Fragment? = null
        if (horizontalScrollFragment.isAdded) {
            fragment = horizontalScrollFragment
        }
        if (verticalScrollFragment.isAdded) {
            fragment = verticalScrollFragment
        }
        if (nestedScrollFragment.isAdded) {
            fragment = nestedScrollFragment
        }
        if (testFragment.isAdded) {
            fragment = testFragment
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss()
            return
        }
        super.onBackPressed()
    }
}