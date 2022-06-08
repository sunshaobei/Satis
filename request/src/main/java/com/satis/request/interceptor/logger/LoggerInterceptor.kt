package com.satis.request.interceptor.logger

import com.satis.request.interceptor.logger.JsonFormat.format
import kotlin.jvm.JvmOverloads
import kotlin.Throws
import android.text.TextUtils
import android.util.Log
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.lang.Exception
import okhttp3.Response.Builder
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * Created by sunsh on 18/5/30.
 */
class LoggerInterceptor @JvmOverloads constructor(var mTag: String? = null,val mShowResponse: Boolean = false) :
    Interceptor {

    init {
        if (TextUtils.isEmpty(mTag)) {
            mTag = javaClass.simpleName
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        logForRequest(request)
        val response: Response = chain.proceed(request)
        return logForResponse(response)
    }

    private fun logForResponse(response: Response): Response {
        try {
            if (mShowResponse) {
                Log.e(mTag, "========response start========")
                val builder: Builder = response.newBuilder()
                val clone: Response = builder.build()
                Log.e(mTag, "url : " + clone.request.url)
                Log.e(mTag, "code : " + clone.code)
                Log.e(mTag, "protocol : " + clone.protocol)
                if (!TextUtils.isEmpty(clone.message)) {
                    Log.e(mTag, "message : " + clone.message)
                }
                var body = clone.body
                if (body != null) {
                    val mediaType = body.contentType()
                    if (mediaType != null) {
                        Log.e(mTag, "responseBody's contentType : $mediaType")
                        if (isText(mediaType)) {
                            val resp = body.string()
                            printLine(true)
                            showLargeLog(mTag!!, "responseBody's content : ${format(resp)}".trimIndent())
                            printLine(false)
                            body = resp.toResponseBody(mediaType)
                            Log.e(mTag, "========response end=========")
                            return response.newBuilder().body(body).build()
                        }
                        Log.e(mTag,
                            "responseBody's content :  maybe [file part] , too large too print , ignored!")
                    }
                }
            }
        } catch (var7: Exception) {
        }
        return response
    }

    private fun logForRequest(request: Request) {
        try {
            if (mShowResponse) {
                val url = request.url.toString()
                val headers = request.headers
                Log.e(mTag, "========request start=======")
                Log.e(mTag, "method : " + request.method)
                Log.e(mTag, "url : $url")
                if (headers.size > 0) {
                    printLine(true)
                    Log.e(mTag, "headers : $headers")
                    printLine(false)
                }
                val requestBody = request.body
                if (requestBody != null) {
                    val mediaType = requestBody.contentType()
                    if (mediaType != null) {
                        Log.e(mTag, "requestBody's contentType : $mediaType")
                        if (isText(mediaType)) {
                            printLine(true)
                            Log.e(mTag, """
     requestBody's content : 
     ${format(bodyToString(request))}
     """.trimIndent())
                            printLine(false)
                        } else {
                            Log.e(mTag, "requestBody's content :  maybe [file part] , too large too print , ignored!")
                        }
                    }
                }
                Log.e(mTag, "========request end=========")
            }
        } catch (var6: Exception) {
        }
    }

    private fun isText(mediaType: MediaType): Boolean {
        return if (mediaType.type == "text") {
            true
        } else {
            mediaType.subtype == "json" || mediaType.subtype == "xml" || mediaType.subtype == "html" || mediaType.subtype == "webviewhtml"
        }
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (var4: IOException) {
            "something error when show requestBody."
        }
    }

    /**
     * 分段打印出较长log文本
     *
     * @param msg 打印文本
     */
    private fun showLargeLog(tag: String, msg: String) {
        var msg = msg
        val max_str_length = 2001 - tag.length
        while (msg.length > max_str_length) {
            Log.e(tag, msg.substring(0, max_str_length))
            msg = msg.substring(max_str_length)
        }
        //剩余部分
        Log.e(tag, msg)
    }

    companion object {
        fun printLine(isTop: Boolean) {
            if (isTop) {
                Log.e(":",
                    "╔═══════════════════════════════════════════════════════════════════════════════════════")
            } else {
                Log.e(":",
                    "╚═══════════════════════════════════════════════════════════════════════════════════════")
            }
        }
    }
}