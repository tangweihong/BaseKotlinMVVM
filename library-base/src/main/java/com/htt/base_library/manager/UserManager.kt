package com.gongziyi.appcore.manager

import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.htt.base_library.manager.ActivityManager
import com.htt.base_library.manager.UserBean
import com.htt.base_library.util.toJson
import com.tencent.mmkv.MMKV
import java.util.*

object UserManager {


    private const val LANGUAGE = "language"
    private const val IS_LOGIN = "is_login"
    private const val TOKEN = "token"
    private const val USER_ID = "user_id"
    private const val USER_NAME = "user_name"
    private const val WS_TOKEN = "ws_token"

    private const val IS_SET_PASSWORD = "is_set_paypwd" //是否设置安全密码

    private const val ACCOUNT_LIST = "account_list" //多账号，列表

    private val mmkv by lazy {
        MMKV.defaultMMKV()
    }

    /**是否以登录*/
    fun isLogin() =
        mmkv.decodeBool(IS_LOGIN, false)

    /**获取当前用户Token*/
    fun getToken(): String? =
        mmkv.decodeString(TOKEN)

    /**获取当前用户名*/
    fun getUserName(): String? =
        mmkv.decodeString(USER_NAME)

    /**获取当前用户Id*/
    fun getUserId(): String? =
        mmkv.decodeString(USER_ID)

    /**获取当前用户wsToken*/
    fun getWsToken(): String? =
        mmkv.decodeString(WS_TOKEN)

    fun getUserBean(): UserBean? = try {
        UserBean(getToken()!!, getUserName()!!, getUserName()!!, getWsToken()!!,
            if (isSetPassword()) 1 else 0)
    } catch (e: java.lang.Exception) {
        null
    }


    /**获取当前语言*/
    fun getLanguage(): String =
        mmkv.decodeString(LANGUAGE, "zh")

    /**缓存当前语言*/
    fun putLanguage(value: String) =
        mmkv.encode(LANGUAGE, value)

    /**是否设置安全密码*/
    fun isSetPassword() = mmkv.decodeInt(IS_SET_PASSWORD, 0) == 1

    /**缓存以设置安全密码*/
    fun putSetPassWord() {
        mmkv.encode(IS_SET_PASSWORD, 1)
        findAccountById(getUserId() ?: "")?.let {
            it.is_set_paypwd = 1
            addOrReplaceAccount(it)
        }
    }

    fun putLogin(login: Boolean) =
        mmkv.encode(IS_LOGIN, login)


    /**保存登录信息*/
    fun saveLogin(userBean: UserBean) {
        mmkv.encode(TOKEN, userBean.token)
        mmkv.encode(WS_TOKEN, userBean.wstoken)
        mmkv.encode(USER_ID, userBean.user_id)
        mmkv.encode(USER_NAME, userBean.username)
        mmkv.encode(IS_SET_PASSWORD, userBean.is_set_paypwd)
    }

    /**切换登录*/
    fun switchLogin(userBean: UserBean) {
        mmkv.encode(TOKEN, userBean.token)
        mmkv.encode(WS_TOKEN, userBean.wstoken)
        mmkv.encode(USER_ID, userBean.user_id)
        mmkv.encode(USER_NAME, userBean.username)
        mmkv.encode(IS_SET_PASSWORD, userBean.is_set_paypwd)
    }


    /**被登出*/
    fun logOut() {
        ToastUtils.showShort("账号失效，请重新登录！")
        onLogout()
    }

    /**手动登出*/
    fun onLogout() {
        try {
            removeValue(
                TOKEN,
                WS_TOKEN,
                USER_ID,
                USER_NAME,
                IS_SET_PASSWORD,
                IS_LOGIN)
//            ActivityManager.finishAllActivity()
//            navigationActivity(null, UserPath.USER_LOGIN)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeValue(vararg key: String) =
        mmkv.removeValuesForKeys(key)


    /*====================================历史账号操作=========================================*/
    /**
     * 获取历史账户列表
     *
     * @return 历史登录||导入账号列表
     */
    fun getAccountList(): MutableList<UserBean> {
        val gson = Gson()
        val json = mmkv.decodeString(ACCOUNT_LIST)
        if (json.isNullOrEmpty()) return arrayListOf()
        try {
            return gson.fromJson(json, Array<UserBean>::class.java).toMutableList()
        } catch (e: Exception) {
            return arrayListOf()
        }
    }

    /**
     * 多账号列表中是否存
     * @param userId 用户id
     */
    fun isAccountListContains(userId: String): Boolean {
        val json = mmkv.decodeString(ACCOUNT_LIST)
        if (json.isNullOrEmpty()) return false
        return json.contains("\"user_id\":\"$userId\"")
    }

    /**
     * 通过id查找历史账号
     *
     * @return userBean
     */
    fun findAccountById(userId: String): UserBean? {
        return getAccountList()
            .find { it.user_id == userId }
    }


    /**
     * 移除账户历史账号
     *
     * @param userId 账号Id
     */
    fun removeAccountById(userId: String) {
        val accountList = getAccountList()
        accountList.find { it.user_id == userId }?.let {
            accountList.remove(it)
        }
        mmkv.encode(ACCOUNT_LIST, accountList.toJson())
    }

    /**
     * 移除账户历史账号
     *
     * @param userBean 账号
     */
    fun removeAccount(userBean: UserBean) {
        val accountList = getAccountList()
        accountList.remove(userBean)
        mmkv.encode(ACCOUNT_LIST, accountList.toJson())
    }

    /**
     * 添加||替换历史账号
     *
     * @param userBean 账号信息实体
     */
    fun addOrReplaceAccount(userBean: UserBean) {
        val accountList = getAccountList()
        if (!isAccountListContains(userBean.user_id)) {
            accountList.add(userBean)
        } else {
            accountList
                .find { it.user_id == userBean.user_id }
                ?.let {
                    it.token = userBean.token
                    it.user_id = userBean.user_id
                    it.username = userBean.username
                    it.wstoken = userBean.wstoken
                    it.is_set_paypwd = userBean.is_set_paypwd
                } ?: accountList.add(userBean)
        }
        mmkv.encode(ACCOUNT_LIST, accountList.toJson())
    }


    /**
     * 添加||替换历史账号
     *
     * @param userBean 账号信息实体
     */
    fun addOrReplaceCurrent() {
        getUserBean()?.let { userBean ->
            val accountList = getAccountList()
            if (!isAccountListContains(userBean.user_id)) {
                accountList.add(userBean)
            } else {
                accountList
                    .find { it.user_id == userBean.user_id }
                    ?.let {
                        it.token = userBean.token
                        it.user_id = userBean.user_id
                        it.username = userBean.username
                        it.wstoken = userBean.wstoken
                        it.is_set_paypwd = userBean.is_set_paypwd
                    } ?: accountList.add(userBean)
            }
            mmkv.encode(ACCOUNT_LIST, accountList.toJson())
        }
    }
    /*====================================历史账号信息=========================================*/

}