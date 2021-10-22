package com.lianshang.mvvm.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.htt.base_library.base.BaseVModel
import com.lianshang.mvvm.model.ExchangeListEntity
import com.lianshang.mvvm.repository.LPRepository

/**
 *
 * Created by hong on 2021/7/3 14:09.
 *
 */
class OpenAccountModel : BaseVModel() {

    val openAccountList: MutableLiveData<ExchangeListEntity> = MutableLiveData()
    private val openAccountRepository: LPRepository by lazy {
        LPRepository()
    }

    fun getMainData() {
        launch({
            openAccountRepository.exchangeList()
        }, { bean ->
            bean.data?.let {
                openAccountList.value = it
            }
        })

    }
}