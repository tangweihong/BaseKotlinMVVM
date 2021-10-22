package com.htt.base_library.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.LogUtils
import com.htt.base_library.util.ClassUtil
import com.htt.base_library.util.showToast
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : BaseVModel, VB : ViewBinding> : Fragment() {
    lateinit var viewModel: VM
    /**
     * ViewBinding
     */
    protected lateinit var mViewBinding: VB

    protected lateinit var mRootView: View

    var loadService: LoadService<*>? = null
    protected lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = requireContext()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments.last() as Class<*>

        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        mViewBinding = method.invoke(null, layoutInflater) as VB
        mRootView = mViewBinding.root
        initLoadService(getLoadView())
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView(view, savedInstanceState)
        setEventListener()
        loadData()
    }

    open fun initViewModel() {
        var classType = ClassUtil.getViewModel<VM>(this)
        val mActivity = activity
        if (classType != null && mActivity != null) {
            viewModel = ViewModelProvider(mActivity).get(classType)
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


    protected open fun initView(view: View, savedInstanceState: Bundle?) {

    }

    open fun loadData() {}

    open fun getLoadView(): View? {
        return null
    }

    private fun initLoadService(view: View?) {
        loadService = view?.let {
            LoadSir.getDefault().register(it) { onReload(view) }
        }

    }

    open fun onReload(view: View?) {

    }

    protected open fun setEventListener() {

    }

    fun showError(msg: String, code: Int) {
        dismissLoadingDialog()
        showToast(msg)

    }

    fun showLoadingDialog(msg: String) {
        activity?.let {
            if (it is BaseActivity<*>) {
                it.showLoadingDialog(msg)
            }
        }
    }

    fun dismissLoadingDialog() {
        activity?.let {
            if (it is BaseActivity<*>)
                it.dismissLoadingDialog()
        }
    }

}