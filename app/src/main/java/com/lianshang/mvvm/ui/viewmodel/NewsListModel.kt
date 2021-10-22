package com.lianshang.mvvm.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.htt.base_library.base.BaseVModel
import com.lianshang.mvvm.model.ExchangeListEntity
import com.lianshang.mvvm.model.NewsListEntity
import com.lianshang.mvvm.repository.LPRepository

/**
 *
 * Created by hong on 2021/7/3 14:09.
 *
 */
class NewsListModel : BaseVModel() {

    val newsList: MutableLiveData<NewsListEntity> = MutableLiveData()
    private val newListRepository: LPRepository by lazy {
        LPRepository()
    }


    fun getNewsList(pagerNumber: Int, isFirst: Boolean) {
        launch({
            newListRepository.getNewsList()
        }, { bean ->
            bean.data?.let {
                newsList.value = it
            }
        }, isShowDialog = isFirst)

    }
}