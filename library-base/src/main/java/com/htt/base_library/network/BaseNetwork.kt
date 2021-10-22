package com.htt.base_library.network

import com.blankj.utilcode.util.SDCardUtils
import com.blankj.utilcode.util.Utils
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class BaseNetwork {
    private val baseUrl:String?
    private var okHttpClient:OkHttpClient?=null
    var isDebug=true

    constructor(url: String?){
        baseUrl=url
    }

    constructor(url:String?,debug:Boolean){
        baseUrl=url
        isDebug=debug
    }

    companion object{
        var DEFAULT_TIME_OUT=10000L

        var retrofitMap=ConcurrentHashMap<String, Retrofit>()
    }


    private fun getRetrofit():Retrofit?{
        return baseUrl?.let {
            var retrofit= retrofitMap[it]
            if(retrofit==null) {
                val retrofitBuilder = Retrofit.Builder()
                retrofitBuilder.baseUrl(it)
                retrofitBuilder.client(createOkHttpClient())

                retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
                //retrofitBuilder.addCallAdapterFactory(LiveDataCallAdapterFactory())
                retrofit = retrofitBuilder.build()
                retrofitMap[it] = retrofit
            }
            retrofit
        }
    }

    private fun createOkHttpClient():OkHttpClient?{
        if(okHttpClient==null){
            val okHttpClientBuilder = OkHttpClient.Builder()

            val cacheSize = 100 * 1024 * 1024 // 10MB
            val cacheDir=if(SDCardUtils.isSDCardEnableByEnvironment()) Utils.getApp().externalCacheDir else Utils.getApp().cacheDir

            cacheDir?.also {
                okHttpClientBuilder.cache(
                    Cache(
                        it, cacheSize.toLong())
                )
            }
            //配置超时时间
            okHttpClientBuilder.connectTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)
            okHttpClientBuilder.writeTimeout(DEFAULT_TIME_OUT,TimeUnit.SECONDS)

            configOkHttp(okHttpClientBuilder)
            if (isDebug) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
            }
            okHttpClient = okHttpClientBuilder.build()
        }
        return okHttpClient
    }

    fun <T> create(service: Class<T>?): T =
        getRetrofit()?.create(service!!) ?: throw RuntimeException("Api service is null!")

    /**
     * 配置Okhttp拦截器
     */
    abstract fun configOkHttp(builder:OkHttpClient.Builder?)


}