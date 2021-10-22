package com.lianshang.mvvm.app

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ProcessUtils
import com.htt.base_library.base.BaseApplication
import com.lianshang.mvvm.R
import com.lianshang.mvvm.common.AppInfo
import com.lianshang.mvvm.network.NetworkApi
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV

class MyApp : BaseApplication() {

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.transparent, R.color.transparent) //全局设置主题颜色
            MaterialHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    companion object {
        var mContext: MyApp? = null
        fun getContext(): Context {
            return mContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        if (ProcessUtils.isMainProcess()) {
            var dirPath = MMKV.initialize(this)
            LogUtils.d("dirPath：$dirPath")
            NetworkApi.initNetworkApi(AppInfo.BASE_URL)
        }
    }
}