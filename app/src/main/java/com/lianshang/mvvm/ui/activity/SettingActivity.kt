package com.lianshang.mvvm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Window
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.htt.base_library.base.BaseListActivity
import com.htt.base_library.base.NoViewModel
import com.htt.base_library.util.showToast
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lianshang.mvvm.databinding.ActivitySettingBinding
import com.lianshang.mvvm.model.LoginEntity
import com.lianshang.mvvm.service.TestService
import com.lianshang.mvvm.ui.adapter.AssetRecordAdapter

class SettingActivity :
    BaseListActivity<AssetRecordAdapter, String, NoViewModel, ActivitySettingBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // 开启Material动画
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)
        // 设置进入的动画
        window.enterTransition = Slide()
        // 设置退出动画
        window.exitTransition = Slide()

    }

    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {
        super.initView(intent, savedInstanceState)
        val intent = Intent()
        intent.putExtra("work", "My Name is Service")
        TestService.enqueueWork(this, intent)
        getAdapter().setOnItemClickListener { _, _, _ ->
            LiveEventBus.get("type1").post("dfaf");

            XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {

                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        super.onDenied(permissions, never)
                        XXPermissions.startPermissionActivity(this@SettingActivity)
                    }
                })

            XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .request(OnPermissionCallback { permissions, all ->

                    permissions.size
                    if (all) "" else "1"
                })


            getAdapter().data.filter {
                it == "2"
            }.forEach {
                LogUtils.e("eeee $it")
            }

        }
        LiveEventBus
            .get("type", LoginEntity::class.java)
            .observe(this, Observer {
                showToast(it.token)
            })
    }


    override fun setEventListener() {
        mViewBinding.button.setOnClickListener {
           onSuccessList(listOf("1", "2", "2", "2", "2", "2").toMutableList())
        }
    }

    override fun loadData() {
        onSuccessList(listOf("1", "2", "2", "2", "2", "2").toMutableList())
    }


    override fun onLoadData(pagerNumber: Int) {
        onSuccessList(mutableListOf("1", "2", "2", "2", "2", "2"))
    }


}