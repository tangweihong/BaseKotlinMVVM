package com.lianshang.mvvm.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.htt.base_library.base.BaseVModel
import com.lianshang.mvvm.model.LoginEntity
import com.lianshang.mvvm.repository.LPRepository

/**
 *
 * Created by hong on 2021/7/3 14:09.
 *
 */
class LoginModel : BaseVModel() {

    val loginData: MutableLiveData<LoginEntity> = MutableLiveData()
    private val loginRepository: LPRepository by lazy {
        LPRepository()
    }

    fun doLogin(account: String, password: String) {
        launch({
            loginRepository.login(account, password)
        }, { bean ->
            bean.data?.let {
                loginData.value = it
            }
        })

    }
}