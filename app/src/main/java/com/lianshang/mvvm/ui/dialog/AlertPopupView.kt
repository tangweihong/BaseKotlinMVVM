package com.lianshang.mvvm.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ClickUtils
import com.lianshang.mvvm.R
import com.lianshang.mvvm.databinding.LayoutAlertPopupViewBinding
import com.lxj.xpopup.core.CenterPopupView

class AlertPopupView : CenterPopupView, View.OnClickListener {
    var title: String = ""
    var content: String = ""
    var cancelText = ""
    var okText = ""
    var listener: OnHandleAlertListener
    var hasCancel = true

    constructor(context: Context,
                title: String,
                content: String,
                cancelText: String = "取消",
                okText: String = "确定",
                listener: OnHandleAlertListener,
                hasCancel: Boolean = true
                ) : super(context) {
        this.title = title
        this.content = content
        this.cancelText = cancelText
        this.okText = okText
        this.listener = listener
        this.hasCancel = hasCancel
    }

    override fun getImplLayoutId(): Int {
        return R.layout.layout_alert_popup_view
    }

    override fun onCreate() {
        super.onCreate()
        val mViewBinding = LayoutAlertPopupViewBinding.bind(popupImplView)
        mViewBinding.tvTitle.text = title
        mViewBinding.tvContent.text = content
        mViewBinding.tvCancel.text = cancelText
        mViewBinding.tvConfirm.text = okText
        if (!hasCancel) {
            mViewBinding.dividerLine.visibility = View.GONE
            mViewBinding.tvCancel.visibility = View.GONE
        }
        ClickUtils.applySingleDebouncing(mViewBinding.tvCancel, this)
        ClickUtils.applySingleDebouncing(mViewBinding.tvConfirm, this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_cancel -> listener.onClickCancel(this)
            R.id.tv_confirm -> listener.onClickConfirm(this)
        }
    }

    interface OnHandleAlertListener {
        fun onClickCancel(popupView: AlertPopupView)
        fun onClickConfirm(popupView: AlertPopupView)
    }
}