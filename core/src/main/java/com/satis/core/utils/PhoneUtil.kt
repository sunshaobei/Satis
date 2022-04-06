package com.satis.core.utils

import android.content.Intent
import android.net.Uri
import com.satis.core.AppContext.context

/**
 * Created by sunshaobei on 2022/4/2.
 */
object PhoneUtil {
    fun callPhone(phone: String) {
        val intentphone = Intent()
        intentphone.action = Intent.ACTION_DIAL // android.intent.action.DIAL
        intentphone.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intentphone.data = Uri.parse("tel:$phone")
        context.startActivity(intentphone)
    }
}