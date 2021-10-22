package com.lianshang.mvvm.app

import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.*
import java.io.File

class AppUpdateService {
    private var mDownloadManager: DownloadManager? = null
    private var downloadId: Long = 0
    private var apkName: String? = null

    fun download(url: String,name:String="app.apk"){
        val packName="com.android.providers.downloads"
        var mContext=Utils.getApp()
        var state=mContext.packageManager.getApplicationEnabledSetting(packName)
        //检测下载管理器是否被禁用
        if (state === PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || state === PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state === PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            val builder: AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(mContext).setTitle("温馨提示")
                    .setMessage("系统下载管理器被禁止，需手动打开")
                    .setPositiveButton("确定") { dialog, which ->
                        dialog.dismiss()
                        try {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.setData(Uri.parse("package:$packName"))
                            mContext.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                            mContext.startActivity(intent)
                        }
                    }.setNegativeButton("取消") { dialog, which -> dialog.dismiss() }

            var dialog=builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)
            dialog.show()
        }else{
            //正常下载流程
            apkName = name
            LogUtils.d("下载链接：$url")
            val request = DownloadManager.Request(Uri.parse(url))
            request.setAllowedOverRoaming(false)

            //通知栏显示
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE
                    or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setTitle(AppUtils.getAppName())
            request.setDescription("正在下载中...")
            request.setVisibleInDownloadsUi(true)

            //设置下载的路径
            val file = File(PathUtils.getExternalDownloadsPath(),apkName)
            request.setDestinationUri(Uri.fromFile(file))

            //request.setDestinationInExternalPublicDir(PathUtils.getExternalDownloadsPath(), apkName)
            //获取DownloadManager

            //获取DownloadManager
            mDownloadManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = mDownloadManager!!.enqueue(request)
            LogUtils.d("开启下载任务...")
            mContext.registerReceiver(mReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        }
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            LogUtils.d("检查下载状态...")
            checkStatus()
        }
    }

    /**
     * 检查下载状态
     */
    private fun checkStatus() {
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor: Cursor = mDownloadManager!!.query(query)
        if (cursor.moveToFirst()) {
            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_PAUSED -> {
                    LogUtils.d("下载暂停。。。")
                }
                DownloadManager.STATUS_PENDING -> {
                    LogUtils.d("下载延迟")
                }
                DownloadManager.STATUS_RUNNING -> {
                    LogUtils.d("正在下载")
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    LogUtils.d("下载完成...")
                    installAPK()
                }
                DownloadManager.STATUS_FAILED -> {
                    LogUtils.d("下载失败!")
                    ToastUtils.showShort("下载失败")
                }
            }
        }
        cursor.close()
    }

    private fun installAPK(){
        var file=File(PathUtils.getExternalDownloadsPath(), apkName)
        LogUtils.d(file.absolutePath)
        //开始安装APK
        AppUtils.installApp(file)
    }
}