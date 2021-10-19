package com.satis.example.overscroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.satis.example.R
import com.satis.example.overscroll.HorizontalScrollFragment.Companion.initHorizontal
import com.satist.overscroll.IOverScrollView
import com.satist.overscroll.IOffsetChangeListener

/**
 * @author ziwei huang
 */
class NestedScrollFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_scrollview, container, false)
        val iconHeaderView = view.findViewById<View>(R.id.icon_header)
        val iconFooterView = view.findViewById<View>(R.id.icon_footer)
        val overScrollView: IOverScrollView = view.findViewById(R.id.overscroll_view)
        overScrollView.addOffsetChangeListener(object : IOffsetChangeListener {
            override fun onOffsetChanged(child: View, offset: Int) {
                if (child!!.height == 0) {
                    return
                }
                val absOffset = Math.abs(offset)
                val scale = 3 * absOffset * 1f / child.height
                if (offset >= 0) {
                    iconHeaderView.pivotX = (iconHeaderView.width / 2).toFloat()
                    iconHeaderView.pivotY = 0f
                    iconHeaderView.scaleX = scale
                    iconHeaderView.scaleY = scale
                    iconFooterView.scaleX = 0f
                    iconFooterView.scaleY = 0f
                } else {
                    iconFooterView.pivotX = (iconFooterView.width / 2).toFloat()
                    iconFooterView.pivotY = iconFooterView.height.toFloat()
                    iconFooterView.scaleX = scale
                    iconFooterView.scaleY = scale
                    iconHeaderView.scaleX = 0f
                    iconHeaderView.scaleY = 0f
                }
            }
        })
        initHorizontal(view.findViewById(R.id.horizontal_view))
        return view
    }
}