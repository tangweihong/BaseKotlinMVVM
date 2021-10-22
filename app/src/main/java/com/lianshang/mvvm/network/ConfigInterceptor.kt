package com.lianshang.mvvm.network

import android.text.TextUtils
import com.lianshang.mvvm.model.TabEntity
import com.tencent.mmkv.MMKV
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Created by hong on 2021/7/27 15:13.
 */
class ConfigInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val mmkv = MMKV.defaultMMKV()
        val originalRequest: Request = chain.request()
        val token: String =
            if (TextUtils.isEmpty(mmkv.decodeString("token"))) "" else mmkv.decodeString("token")
        val headers = originalRequest.headers.newBuilder()
            .add("Accept", "application/json")
            .add("Content-Type", "application/json")
            .add("token", token)
            .build()
        val request = originalRequest.newBuilder().headers(headers).build()
        return chain.proceed(request)
    }
}
