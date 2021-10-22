package com.htt.base_library.manager

import android.app.Activity
import android.os.Process
import java.util.*
import kotlin.system.exitProcess


/**
 * activity栈管理
 */
object ActivityManager {


    private val mActivitySet by lazy {
        Stack<Activity>()
    }


    fun addActivity(activity: Activity) =
        mActivitySet.add(activity)


    fun removeActivity(activity: Activity) =
        mActivitySet.remove(activity)


    fun getActivity(clazz: Class<*>): Activity? =
        mActivitySet.find { clazz == it.javaClass }


    fun getCurrentActivity(): Activity? =
        mActivitySet.lastElement()


    fun getAllActivityStacks(): Stack<Activity> = mActivitySet

    /**
     * 结束指定类名的 Activity
     */
    fun finishActivity(cls: Class<*>) = finishActivity(getActivity(cls))

    /**
     * 结束指定的 Activity
     */
    fun finishActivity(activity: Activity?) = activity?.let {
        if (!it.isFinishing) {
            it.finish()
            mActivitySet.remove(it)
        }
    }

    /**
     * 结束除当前传入以外所有 Activity
     */
    fun finishOthersActivity(cls: Class<*>) {
        mActivitySet.filter { cls != it.javaClass }.forEach {
            it.finish()
        }
    }

    fun finishAllActivity(){
        mActivitySet.forEach {
            it.finish()
        }
        mActivitySet.clear()
    }


    /**
     * 退出 app 时调用
     */
    fun exitApp() {
        try {
            finishAllActivity()
            Process.killProcess(Process.myPid())
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取一个对象的独立无二的标记
     */
    private fun getObjectTag(calss: Any): String {
        // 对象所在的包名 + 对象的内存地址
        return calss.javaClass.name + Integer.toHexString(calss.hashCode())
    }

}