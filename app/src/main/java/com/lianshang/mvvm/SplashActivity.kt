package com.lianshang.mvvm

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.blankj.utilcode.util.*
import com.gyf.immersionbar.ImmersionBar
import com.htt.base_library.base.BaseActivity
import com.lianshang.mvvm.app.AppUserInfo
import com.lianshang.mvvm.databinding.ActivitySplashBinding
import com.lianshang.mvvm.network.DownloadUtil
import com.lianshang.mvvm.network.OnDownloadListener
import com.lianshang.mvvm.ui.MainActivity
import com.lianshang.mvvm.ui.account.LoginActivity
import com.lianshang.mvvm.ui.dialog.AlertPopupView
import com.lianshang.mvvm.ui.dialog.AppUpdatePopupView
import com.lianshang.mvvm.ui.dialog.DialogHelper
import com.wuyr.activitymessenger.startActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SplashActivity : BaseActivity<ActivitySplashBinding>() {


    override fun initView(intent: Intent?, savedInstanceState: Bundle?) {

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot) {
            val intent = getIntent()
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish()
                    return
                }
            }
        }
    }

    override fun loadData() {
        checkPermission()
    }

    //权限检查
    private fun checkPermission() {
        PermissionUtils.permission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).callback { _, _, _, _ ->
            mViewBinding.lottieView.playAnimation()
            initAnimation()
        }.request()
    }

    private fun goNextPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (AppUserInfo.isLogin()) {
                startActivity<MainActivity>()
            } else {
                startActivity<LoginActivity>()
            }
            finish()
        }, 2000)
    }

    fun initAnimation() {
        val list = mutableListOf(
            "pride-hard-seltzer.json",
            "lamsa-splash-screen.json",
            "ramadan-kareem-hello-doc.json",
            "logo-animation.json"
        )

        mViewBinding.lottieView.setAnimation(list.random())
        mViewBinding.lottieView.addAnimatorListener(object :Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (AppUserInfo.isLogin()) {
                    startActivity<MainActivity>()
                } else {
                    startActivity<LoginActivity>()
                }
                finish()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
//        mViewBinding.lottieView.addAnimatorUpdateListener {
//            if (it.animatedFraction == 1f) {
//                if (AppUserInfo.isLogin()) {
//                    startActivity<MainActivity>()
//                } else {
//                    startActivity<LoginActivity>()
//                }
//                finish()
//            }
//        }
    }

    private fun getAppDownloadPath(): String {
        return if (SDCardUtils.isSDCardEnableByEnvironment()) {
            PathUtils.getExternalDownloadsPath() + File.separator + AppUtils.getAppName() + ".apk"
        } else {
            PathUtils.getDownloadCachePath() + File.separator + AppUtils.getAppName() + ".apk"
        }
    }

    fun getAppUpdate() {
        var updatePath =
            "https://imtt.dd.qq.com/16891/apk/FA48766BA12A41A1D619CB4B152889C6.apk?fsname=com.estrongs.android.pop_4.2.3.3_10089.apk&csr=1bbd";

        DialogHelper.showAppUpdateDialog(this@SplashActivity,
            "最新版本：1.0.1",
            "vInfo",
            1,
            object : AppUpdatePopupView.OnDownloadListener {
                override fun onClickDownload(view: AppUpdatePopupView) {
                    startDownload(updatePath, 1, view)
                }

                override fun onClickClose(view: AppUpdatePopupView) {
                    goNextPage()
                }
            })
    }

    private fun startDownload(
        downloadUrl: String,
        flag: Int,
        view: AppUpdatePopupView
    ) {
        DownloadUtil.instance.download(downloadUrl, getAppDownloadPath(), object :
            OnDownloadListener {
            override fun onDownloadSuccess(filePath: String) {
                ToastUtils.showShort("下载成功!")
                launch(Dispatchers.Main) {
                    var file = File(filePath)
                    LogUtils.d(file.absolutePath)
                    //开始安装APK
                    AppUtils.installApp(file)
                    if (flag != 9) {
                        goNextPage()
                    } else {
                        finish()
                    }
                }
            }

            override fun onDownloadProgress(progress: Int) {
                LogUtils.d("下载进度：$progress")
                launch(Dispatchers.Main) {
                    view.setUpdate(progress)
                }
            }

            override fun onDownloadFail(msg: String) {
                launch(Dispatchers.Main) {
                    DialogHelper.showAlertDialog(
                        this@SplashActivity,
                        "提示",
                        "下载失败！是否重新下载?",
                        "取消",
                        "重新下载",
                        object : AlertPopupView.OnHandleAlertListener {
                            override fun onClickCancel(popupView: AlertPopupView) {
                                popupView.dismiss()
                                if (flag == 9)
                                    finish()
                                else {
                                    view.dismiss()
                                    goNextPage()
                                }
                            }

                            override fun onClickConfirm(popupView: AlertPopupView) {
                                popupView.dismiss()
                                startDownload(downloadUrl, flag, view)
                            }
                        }, backDismiss = false
                    )
                }
            }
        })
    }

    override fun onDestroy() {
        DownloadUtil.instance.cancelDownload()
        super.onDestroy()
    }

}