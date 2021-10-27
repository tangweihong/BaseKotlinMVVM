package com.htt.base_library.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.RomUtils
import com.gongziyi.appcore.constant.Constant
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import com.htt.base_library.util.DialogUtils
import com.htt.base_library.util.ScreenUtils
import com.htt.base_library.util.showToast
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import me.leefeng.promptlibrary.PromptDialog
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {
    var loadService: LoadService<*>? = null

    var loadingDialog: PromptDialog? = null

    /**
     * ViewBinding
     */
    protected lateinit var mViewBinding: VB
    protected var mContext: Context? = null

    /**
     * RootView
     */
    private lateinit var mRootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //反射获取ViewBing
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments.last() as Class<VB>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        mViewBinding = method.invoke(null, layoutInflater) as VB
        setContentView(mViewBinding?.root)
        mContext = this
        if (isImmersionBar()) initImmersionBar()
        initLoadService(getLoadView())
        initView(intent, savedInstanceState)
        setEventListener()
        loadData()
    }


    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }


    abstract fun initView(intent: Intent?, savedInstanceState: Bundle?)

    open fun loadData(){}

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

    open fun isImmersionBar(): Boolean {
        return true
    }

    open fun getTitleBar(): View? {
        return null
    }

    open fun initImmersionBar() {
        //在BaseActivity里初始化
        immersionBar {
            statusBarColor(android.R.color.transparent)
            if (!RomUtils.isVivo()) {
                transparentBar()
            }
            keyboardEnable(enableKeyboard())
            hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            if (RomUtils.isVivo()) {
                statusBarDarkFont(isDartFont())
            } else {
                statusBarDarkFont(isDartFont(), 0.2f)
            }
            getTitleBar()?.apply {
                titleBar(this)
            }
//            init()
        }
    }

    protected open fun isDartFont(): Boolean = true

    protected open fun enableKeyboard() = false

    protected open fun setEventListener() {

    }


    open fun showError(msg: String, code: Int) {
        dismissLoadingDialog()
        showToast(msg)
        if (code == Constant.HTTP_CODE_LOGOUT) {

        }
    }

    open fun showLoadingDialog(msg: String) {
        showLoadingDialog(true)
    }

    open fun dismissLoadingDialog() {
        DialogUtils.dismiss()
    }

    open fun showLoadingDialog(isCancelable: Boolean) {
        showLoadingDialog(isCancelable, "")
    }

    open fun showLoadingDialog(isCancelable: Boolean, msg: String?) {
        DialogUtils.showLoadingDialog(this, msg, isCancelable)
    }

    /**
     * 点击空白区域隐藏键盘.
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (null != this.currentFocus) {
            val mInputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            return mInputMethodManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            ScreenUtils.hideKeyboard(ev, currentFocus, this@BaseActivity) //调用方法判断是否需要隐藏键盘
        }

        return super.dispatchTouchEvent(ev)
    }
}