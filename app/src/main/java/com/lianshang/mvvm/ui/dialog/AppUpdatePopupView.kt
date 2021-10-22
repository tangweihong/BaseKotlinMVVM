package com.lianshang.mvvm.ui.dialog

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ClickUtils
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.LayoutAlertPopupViewBinding
import com.lianshang.mvvm.databinding.LayoutUpdateAppPopupViewBinding
import com.lianshang.mvvm.ui.widget.CircleProgressBar
import com.lianshang.mvvm.util.ToastUtil
import com.lxj.xpopup.core.CenterPopupView

class AppUpdatePopupView : CenterPopupView {
    var versionInfo: String = ""
    var updateInfo: String = ""
    var updateFlag: Int = 1
    var listener: AppUpdatePopupView.OnDownloadListener

    constructor(
        context: Context,
        version: String,
        update: String,
        updateFlag: Int,
        listener: AppUpdatePopupView.OnDownloadListener
    ) : super(context) {
        this.versionInfo = version
        this.updateInfo = update
        this.updateFlag = updateFlag
        this.listener = listener
    }

    private lateinit var mViewBinding: LayoutUpdateAppPopupViewBinding
    override fun getImplLayoutId(): Int {
        return R.layout.layout_update_app_popup_view
    }

     fun setUpdate(progress: Int) {
        mViewBinding.circleProgress.setProgress(progress)
    }

    override fun onCreate() {
        super.onCreate()
        mViewBinding = LayoutUpdateAppPopupViewBinding.bind(popupImplView)
        mViewBinding.tvNewVersion.text = versionInfo
        mViewBinding.tvUpdateInfo.text = updateInfo
        if (updateFlag == 1) {
            ClickUtils.applySingleDebouncing(mViewBinding.ivClose) {
                listener.onClickClose(this)
                dismiss()
            }
            ClickUtils.applySingleDebouncing(mViewBinding.tvUpdate) {
                mViewBinding.ivClose.visibility = View.GONE
                mViewBinding.circleProgress.visibility = View.VISIBLE
                mViewBinding.tvUpdate.text = "下载中"
                mViewBinding.tvUpdate.isEnabled = false
                listener.onClickDownload(this)
            }
        } else if (updateFlag == 9) {
            mViewBinding.tvUpdate.text = "强制更新"
            ToastUtil.showShortMsg("3秒后自动下载")
            mViewBinding.tvUpdate.postDelayed({
                mViewBinding.ivClose.visibility = View.GONE
                mViewBinding.circleProgress.visibility = View.VISIBLE
                mViewBinding.tvUpdate.text = "下载中"
                mViewBinding.tvUpdate.isEnabled = false
                listener.onClickDownload(this)
            }, 1000)
        }
    }

    interface OnDownloadListener {
        fun onClickDownload(view: AppUpdatePopupView)
        fun onClickClose(view: AppUpdatePopupView)
    }
}