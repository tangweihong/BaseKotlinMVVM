package com.htt.base_library.base

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.htt.base_library.util.ClassUtil

/**
 *
 * Created by hong on 2021/7/13 9:46.
 *
 */
abstract class BaseVMActivity<VM : BaseVModel, VB : ViewBinding> : BaseActivity<VB>() {

    lateinit var viewModel: VM

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        initViewModel()
    }

    open fun initViewModel() {
        var classType = ClassUtil.getViewModel<VM>(this)
        classType?.let {
            viewModel = ViewModelProvider(this).get(classType)
            lifecycle.addObserver(viewModel)
            registerUIChange()
        }
    }

    /**
     * 注册 UI 事件
     */
    private fun registerUIChange() {
        viewModel.ui.showDialog.observe(this) {
            showLoadingDialog(it ?: "")
        }
        viewModel.ui.dismissDialog.observe(this) {
            dismissLoadingDialog()
        }
        viewModel.ui.errorEvent.observe(this) {
            showError(it.msg, it.code)
        }
    }

}