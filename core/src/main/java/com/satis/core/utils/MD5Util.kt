package com.satis.core.utils

import com.satis.core.utils.MD5Util.getMD5
import java.security.MessageDigest

/**
 * Created by sunshaobei on 2022/4/2.
 */

fun String.md5():String?{
	return getMD5(this.toByteArray())
}

object MD5Util {
	fun getMD5(source: ByteArray?): String? {
		var s: String? = null
		val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f')
		try {
			val md = MessageDigest
				.getInstance("MD5")
			md.update(source)
			val tmp = md.digest()
			val str = CharArray(16 * 2)
			var k = 0
			for (i in 0..15) {
				val byte0 = tmp[i]
				str[k++] = hexDigits[byte0.toInt() ushr 4 and 0xf]
				str[k++] = hexDigits[byte0.toInt() and 0xf]
			}
			s = String(str)
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return s
	}
}