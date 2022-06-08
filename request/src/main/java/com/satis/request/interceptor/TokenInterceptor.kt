package com.satis.request.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by sunshaobei on 2022/4/26.
 */
class TokenInterceptor :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val proceed = chain.proceed(chain.request())
        return proceed
    }
}