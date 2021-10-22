package com.lianshang.mvvm.app

import android.text.TextUtils
import com.tencent.mmkv.MMKV

object AppUserInfo {
    private const val USER_TOKEN = "user_token"
    private const val USER_ACCOUNT = "user_account"
    private const val SAFE_PWD_STATE = "safe_pwd_state"
    private const val USER_TYPE = "user_type"


    fun getToken(): String {
        return MMKV.defaultMMKV().getString(USER_TOKEN, "") ?: ""
    }

    fun setToken(token: String) {
        MMKV.defaultMMKV().putString(USER_TOKEN, token)
    }


    fun setUserType(type: Int = 0) {
        MMKV.defaultMMKV().putInt(USER_TYPE, type)
    }

    fun getUserType(): Int {
        return MMKV.defaultMMKV().getInt(USER_TYPE, 0)
    }


    fun setUserAccount(account: String) {
        MMKV.defaultMMKV().putString(USER_ACCOUNT, account)
    }

    fun getUserAccount(): String {
        return MMKV.defaultMMKV().getString(USER_ACCOUNT, "") ?: ""
    }

    fun setSafePwdState(state: Int) {
        MMKV.defaultMMKV().putBoolean(SAFE_PWD_STATE, state == 1)
    }

    fun hasSafePwd(): Boolean {
        return MMKV.defaultMMKV().getBoolean(SAFE_PWD_STATE, false)
    }

    //判断是否已经登录
    fun isLogin(): Boolean {
        var token = getToken()
        return !TextUtils.isEmpty(token)
    }

    fun clearLogin() {
        setToken("")
        setUserType(0)
    }
}