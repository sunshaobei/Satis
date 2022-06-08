package com.satis.request.core

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import javax.net.ssl.SSLSocketFactory

/**
 * Created by sunshaobei on 2022/4/25.
 */
object Http {
    val client by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .sslSocketFactory(createSSLSocketFactory(), TrustAllManager())
            .hostnameVerifier(TrustAllHostnameVerifier())
            .build()
    }


    @SuppressLint("TrulyRandom")
    fun createSSLSocketFactory(): SSLSocketFactory {
        val sc = SSLContext.getInstance("TLS")
        sc.init(null, arrayOf<TrustManager>(TrustAllManager()),
            SecureRandom())
        return sc.socketFactory
    }

    private class TrustAllManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }

    /**
     * 可访问的 host
     */
    private class TrustAllHostnameVerifier : HostnameVerifier {
        override fun verify(hostname: String?, session: SSLSession?): Boolean {
            return true
        }
    }
}