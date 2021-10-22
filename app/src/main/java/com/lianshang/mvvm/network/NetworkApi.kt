package com.lianshang.mvvm.network

import com.htt.base_library.network.BaseNetwork
import com.lianshang.mvvm.app.AppUserInfo
import okhttp3.OkHttpClient

class NetworkApi(url: String?) : BaseNetwork(url, true) {

    companion object {
        private var baseUrl: String? = null

        @Volatile
        private var instance: NetworkApi? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: NetworkApi(baseUrl).also { instance = it }
        }

        fun initNetworkApi(url: String?) {
            baseUrl = url
        }
    }

    fun getApi(): HttpService {
        return create(HttpService::class.java)
    }

    override fun configOkHttp(builder: OkHttpClient.Builder?) {
        builder?.apply {
            this.addInterceptor {
                var request = it.request()
                request = request
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", AppUserInfo.getToken())
                    .addHeader("language", "zh-CN")
                    .build()
                it.proceed(request)
            }
        }
    }
}