package com.satis.core.component

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.satis.core.Framework
import com.satis.core.databinding.ActivityRootBinding
import com.satis.core.utils.SizeUtil.dp

/**
 * Created by sunshaobei on 2022/3/3.
 * app base activity
 */
abstract class FrameworkActivity : AppCompatActivity() {
    lateinit var mRootBinding: ActivityRootBinding
    lateinit var mRootContent: ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRootBinding = ActivityRootBinding.inflate(layoutInflater)
        mRootContent = mRootBinding.root
        super.setContentView(mRootBinding.root)
    }

    override fun setContentView(layoutResID: Int) {
        setContentView(layoutInflater.inflate(layoutResID, null))
    }

    override fun setContentView(view: View) {
        mRootBinding.root.addView(view)
        if (Framework.showActivityTag){
            showTagIfDebug()
        }
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        mRootBinding.root.addView(view, params)
        if (Framework.showActivityTag){
            showTagIfDebug()
        }
    }


    /**
     * 显示activity 名称
     */
    private fun showTagIfDebug() {
        val tag = TextView(this)
        tag.setBackgroundColor(Color.parseColor("#60ff0000"))
        tag.setTextColor(Color.WHITE)
        tag.text = javaClass.simpleName
        mRootBinding.root.addView(
            tag,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        tag.x = 20.dp
        tag.y = 20.dp
        tag.elevation = 10.dp
    }


}