package com.lianshang.mvvm.network

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.jvm.Throws

/**
 * 解决Glide加载Https图片的问题
 */
@GlideModule
class HttpsGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {}
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        //设置请求方式为okhttp 并设置okhttpClient的证书及超时时间
        val factory = OkHttpUrlLoader.Factory(UnsafeOkHttpClient.unsafeOkHttpClient)
        registry.replace(GlideUrl::class.java, InputStream::class.java, factory)
    }

    //自定义工具类修改OkHttpClient证书和超时时间
    internal object UnsafeOkHttpClient {
        // Create a trust manager that does not validate certificate chains
        val unsafeOkHttpClient: OkHttpClient

        // Install the all-trusting trust manager

            // Create an ssl socket factory with our all-trusting manager
            get() = try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, (trustAllCerts[0] as X509TrustManager))
                builder.hostnameVerifier { hostname, session -> true }
                builder.connectTimeout(20, TimeUnit.SECONDS)
                builder.readTimeout(20, TimeUnit.SECONDS)
                builder.writeTimeout(20, TimeUnit.SECONDS)
                builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        /**
         * 禁止解析Manifest文件
         * 主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误
         * @return
         */
        /* @Override
        public boolean isManifestParsingEnabled() {
            return false;
        }*/
    }
}