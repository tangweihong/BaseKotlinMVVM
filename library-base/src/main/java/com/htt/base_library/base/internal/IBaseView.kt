package com.htt.base_library.base.internal

import android.os.Bundle
import com.test.library_base.R

/**
 * Created by gongziyi on 2021/5/13 18:48.
 * BaseView接口文档
 * 用于保证 Activity/Fragment 接口名或功能一致
 */
interface IBaseView {

    fun getConfig(): FConfig = FConfig(-1)

    fun initView(savedInstanceState: Bundle?) = Unit

    fun initData() = Unit

    fun initListener() = Unit


    /**
     * 错误提示
     * @param msg 文本
     * @param code 错误码
     */
    fun showError(msg: String, code: Int)

    /**
     * 显示loading弹窗
     * @param msg 文本
     * @param isCancelable 是否可取消
     */
    fun showLoadingDialog(msg: String? = null, isCancelable: Boolean = true)

    /**
     * 关闭loading弹窗
     */
    fun dismissLoadingDialog()


    fun showToast(msg: String?, isLong: Boolean = false)
    fun showToast(msgId: Int, isLong: Boolean = false)


}