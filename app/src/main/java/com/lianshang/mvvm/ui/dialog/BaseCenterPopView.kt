package com.lianshang.mvvm.ui.dialog

import android.content.Context
import com.lxj.xpopup.core.CenterPopupView

/**
 *
 * Created by hong on 2021/7/30 16:35.
 *
 */
abstract class BaseCenterPopView(context: Context) : CenterPopupView(context) {
    override fun onCreate() {
        super.onCreate()

    }

    abstract fun getBindingView()
}