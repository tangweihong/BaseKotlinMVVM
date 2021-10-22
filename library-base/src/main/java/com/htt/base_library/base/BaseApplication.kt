package com.htt.base_library.base

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.Utils

open class BaseApplication :MultiDexApplication(){

    override fun onCreate() {
        super.onCreate()
        if(packageName == getCurrentProcessName()){
            //主进程
            Utils.init(this)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    /**
     * 获取当前进程名
     */
    private fun getCurrentProcessName(): String? {
        val pid = Process.myPid()
        var processName = ""
        val manager =
            applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid == pid) {
                processName = process.processName
            }
        }
        return processName
    }
}