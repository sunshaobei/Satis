package com.satis.request

import com.satis.request.core.Http
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by sunshaobei on 2022/4/24.
 */
object Request {
    val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { retrofit() }

   private fun retrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("HttpConfig.BASE_URL")
            .addConverterFactory(GsonConverterFactory.create()) //添加gson转换器
            .client(Http.client)
            .build()
    }

    fun <T> getService(clazz: Class<T>): T {
        return instance.create(clazz) as T
    }


}