package com.lianshang.mvvm.network

import com.htt.base_library.network.BaseResponse
import com.lianshang.mvvm.model.ExchangeListEntity
import com.lianshang.mvvm.model.HomeEntity
import com.lianshang.mvvm.model.LoginEntity
import com.lianshang.mvvm.model.NewsListEntity
import com.lianshang.mvvm.network.NetworkApi
import retrofit2.http.*


interface HttpService {
    companion object {
        fun getHttpService(): HttpService {
            return NetworkApi.getInstance().create(HttpService::class.java)
        }
    }

    /**
     *登录
     * @param account String
     * @param password String
     * @return BaseResponse<LoginEntity>
     */
    @POST("api/login")
    @FormUrlEncoded
    suspend fun senLogin(
        @Field("account") account: String,
        @Field("password") password: String
    ): BaseResponse<LoginEntity>

    /**
     * 首页数据
     * @return BaseResponse<HomeEntity>
     */
    @GET("api/home")
    suspend fun sendHome(): BaseResponse<HomeEntity>

    /**
     * 交易所列表
     * @return BaseResponse<HomeEntity>
     */
    @GET("api/platformList")
    suspend fun getExchangeList(): BaseResponse<ExchangeListEntity>
    /**
     * 交易所列表
     * @return BaseResponse<HomeEntity>
     */
    @GET("api/information")
    suspend fun getNewsList(): BaseResponse<NewsListEntity>
}