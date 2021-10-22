package com.lianshang.mvvm.ui.account

import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ClickUtils
import com.htt.base_library.base.BaseVMActivity
import com.htt.base_library.util.click
import com.lianshang.mvvm.app.AppUserInfo
import com.lianshang.mvvm.databinding.ActivityLoginBinding
import com.lianshang.mvvm.ext.passwordShowHide
import com.lianshang.mvvm.ui.MainActivity
import com.lianshang.mvvm.ui.viewmodel.LoginModel
import com.wuyr.activitymessenger.startActivity

class LoginActivity : BaseVMActivity<LoginModel, ActivityLoginBinding>() {


    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)
    }

    override fun setEventListener() {
        super.setEventListener()
        ClickUtils.applySingleDebouncing(mViewBinding.loginBtnNext) {

            viewModel.doLogin(
                mViewBinding.edtAccount.text.toString(),
                mViewBinding.edtPassword.text.toString()
            )
        }
        mViewBinding.loginImgPasswordEye.setOnClickListener {
            onPassWordEye()
        }

    }

    private var mPassWordEye: Boolean = false
    private fun onPassWordEye() {
        mPassWordEye = !mPassWordEye
        mViewBinding.loginImgPasswordEye.isSelected = mPassWordEye
        mViewBinding.edtPassword.passwordShowHide(mPassWordEye)
    }

    override fun loadData() {
        viewModel.loginData.observe(this) {
            AppUserInfo.setToken(it.token)
            startActivity<MainActivity>()
        }
    }


}