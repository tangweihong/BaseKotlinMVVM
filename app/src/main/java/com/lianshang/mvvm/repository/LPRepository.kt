package com.lianshang.mvvm.repository

import com.htt.base_library.network.BaseResponse
import com.lianshang.mvvm.model.ExchangeListEntity
import com.lianshang.mvvm.model.HomeEntity
import com.lianshang.mvvm.model.LoginEntity
import com.lianshang.mvvm.model.NewsListEntity
import com.lianshang.mvvm.network.HttpService
import com.lianshang.mvvm.network.NetworkApi

/**
 * @author :Charles in  2021/9/18 11:33.
 *         功能描述:
 */
class LPRepository {
    /**
     * 登录
     * @param username String
     * @param password String
     * @return BaseResponse<LoginEntity>
     */
    suspend fun login(username: String, password: String): BaseResponse<LoginEntity> {
        return NetworkApi.getInstance().getApi().senLogin(username, password)
    }

    /**
     *
     * 首页
     * @return BaseResponse<HomeEntity>
     */
    suspend fun homeData(): BaseResponse<HomeEntity> {
        return NetworkApi.getInstance().getApi().sendHome()
    }

    /**
     * API列表
     * @return BaseResponse<ExchangeListEntity>
     */
    suspend fun exchangeList(): BaseResponse<ExchangeListEntity> {
        return NetworkApi.getInstance().getApi().getExchangeList()
    }
    /**
     * API列表
     * @return BaseResponse<ExchangeListEntity>
     */
    suspend fun getNewsList(): BaseResponse<NewsListEntity> {
        return NetworkApi.getInstance().getApi().getNewsList()
    }
}