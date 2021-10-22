package com.lianshang.mvvm.ui.dialog

import android.content.Context
import com.lxj.xpopup.XPopup

object DialogHelper {
    /**
     * 基础弹窗
     */
    fun showAlertDialog(context: Context,
                        title:String,
                        content:String,
                        cancelText:String="取消",
                        okText:String="确定",
                        listener: AlertPopupView.OnHandleAlertListener,
                        hasCancel:Boolean=true,
                        backDismiss:Boolean=true):AlertPopupView{
        var alertPopupView=AlertPopupView(context,title,content,cancelText,okText,listener,hasCancel)
        XPopup.Builder(context)
                .autoDismiss(false)
                .dismissOnTouchOutside(false)
                .dismissOnBackPressed(backDismiss)
                .asCustom(alertPopupView)
                .show()
        return alertPopupView
    }

    /**
     * 更新弹窗
     */
    fun showAppUpdateDialog(context: Context,
                            version:String,
                            update:String,
                            flag:Int,
                            listener:AppUpdatePopupView.OnDownloadListener){
        var updateView=AppUpdatePopupView(context,version,update,flag,listener)
        XPopup.Builder(context)
            .autoDismiss(false)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(updateView)
            .show()
    }

}