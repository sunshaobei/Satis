package com.satis.request.annotation
@Target(
    AnnotationTarget.CLASS,
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class SatisUrl(val host: String)