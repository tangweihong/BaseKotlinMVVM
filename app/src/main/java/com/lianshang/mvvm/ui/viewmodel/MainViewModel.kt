package com.lianshang.mvvm.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.htt.base_library.base.BaseVModel
import com.lianshang.mvvm.model.HomeEntity
import com.lianshang.mvvm.repository.LPRepository

/**
 *
 * Created by hong on 2021/7/3 14:09.
 *
 */
class MainViewModel : BaseVModel() {

    val mainData: MutableLiveData<HomeEntity> = MutableLiveData()
    private val homeRepository: LPRepository by lazy {
        LPRepository()
    }

    val mMainIndex = MutableLiveData<String>();

    fun getMainData(isShow: Boolean) {
        launch({
            homeRepository.homeData()
        }, { bean ->
            bean.data?.let {
                mainData.value = it
            }
        }, isShowDialog = isShow)

    }

    fun setMainTabIndex(index: String) {
        mMainIndex.value = index
    }
}