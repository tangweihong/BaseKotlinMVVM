package com.lianshang.mvvm.service

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.app.JobIntentService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.htt.base_library.util.showToast
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lianshang.mvvm.app.MyApp
import com.lianshang.mvvm.model.LoginEntity

/**
 *
 * Created by hong on 2021/7/16 17:51.
 *
 */
class TestService : JobIntentService(){
    private val mHandler1 = Handler(Looper.getMainLooper()) {
        when (it.what) {
            1 -> {
                showToast("service")
                senMessage()
            }
        }
        false
    }


    private fun senMessage() {
        //秦皇      玛雅        埃及     通天塔
        val login = LoginEntity()
        login.token = "123456"
        LiveEventBus.get("type").post(login);
        mHandler1.sendEmptyMessageDelayed(1, 3000)
    }




    override fun onHandleWork(intent: Intent) {
        LogUtils.e("onHandleWord:------" + intent.getStringExtra("work").toString())
        senMessage()
        LiveEventBus.get("",LoginEntity::class.java).observeForever {

        }
    }

    companion object {
        @JvmStatic
        fun enqueueWork(ApplicationContext: Context, intent: Intent) {
            LogUtils.e("enqueueWork:------" + intent.getStringExtra("work"))
            enqueueWork(ApplicationContext, TestService::class.java, 11111, intent)
        }
    }



}