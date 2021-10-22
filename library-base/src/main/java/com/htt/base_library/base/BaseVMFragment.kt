package com.htt.base_library.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.htt.base_library.util.ClassUtil

/**
 *
 * Created by hong on 2021/7/13 9:46.
 *
 */
abstract class BaseVMFragment<VM : BaseVModel, VB : ViewBinding> : BaseFragment<VM, VB>() {

//    lateinit var viewModel: VM
//
//    override fun initView(view: View, savedInstanceState: Bundle?) {
//        super.initView(view, savedInstanceState)
//        initViewModel()
//    }
//
//    open fun initViewModel() {
//        var classType = ClassUtil.getViewModel<VM>(this)
//        val mActivity = activity
//        if (classType != null && mActivity != null) {
//            viewModel = ViewModelProvider(mActivity).get(classType)
//            lifecycle.addObserver(viewModel)
//            registerUIChange()
//        }
//    }
//
//    /**
//     * 注册 UI 事件
//     */
//    private fun registerUIChange() {
//        viewModel.ui.showDialog.observe(this) {
//            showLoadingDialog(it ?: "")
//        }
//        viewModel.ui.dismissDialog.observe(this) {
//            dismissLoadingDialog()
//        }
//        viewModel.ui.errorEvent.observe(this) {
//            showError(it.msg, it.code)
//        }
//    }

}