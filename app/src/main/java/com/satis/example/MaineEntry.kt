package com.satis.example

import android.app.Activity
import com.satis.example.overscroll.OverScrollActivity
import com.satis.example.sliver.SliverActivity
import com.satis.example.viewmodel.SatisViewModelActivity

enum class MaineEntry(val aClass:Class<out Activity>) {
    SLIVER(SliverActivity::class.java),
    OVERSCROLL(OverScrollActivity::class.java),
    VIEMMODEL(SatisViewModelActivity::class.java);
}