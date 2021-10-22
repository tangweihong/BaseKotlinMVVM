package com.lianshang.mvvm.network

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import okhttp3.*
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception

class DownloadUtil {

    private val okHttpClient=lazy { OkHttpClient() }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            DownloadUtil()
        }
    }

    fun download(url:String,
                 filePath:String,
                 listener:OnDownloadListener){
        var request= Request.Builder().url(url).build()
        okHttpClient.value.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                listener.onDownloadFail(e.message?:"下载失败1！")
            }

            override fun onResponse(call: Call, response: Response) {
                //文件下载
                var inputStream:InputStream?=null
                var buffer= ByteArray(2048)
                var len=0
                var outputStream:OutputStream?=null
                FileUtils.createOrExistsFile(filePath)

                if(response.body==null){
                    listener.onDownloadFail("下载的文件为空!")
                }else{
                    response.body?.also {
                        try {
                            inputStream = it.byteStream()
                            var total = it.contentLength()
                            LogUtils.d("total：$total")
                            var file = FileUtils.getFileByPath(filePath)
                            outputStream = FileOutputStream(file)
                            var sum = 0L
                            do{
//                                LogUtils.d("读取下载数据...")
                                len=inputStream?.read(buffer)?:-1
                                LogUtils.d("len=$len")
                                if(len==-1)
                                    break
                                outputStream?.write(buffer,0,len)
                                sum+=len
                                var progress=(sum*1.0f/total*100).toInt()
                                LogUtils.d("progress:$progress")
                                //下载中
                                listener.onDownloadProgress(progress)
                            }while (len!=-1)
                            outputStream?.flush()
                            listener.onDownloadSuccess(filePath)
                        }catch (e:Exception){
                            listener.onDownloadFail(e.message?:"下载失败2!")
                        }finally {
                            try {
                                inputStream?.close()
                            }catch (e:Exception){

                            }
                            try{
                                outputStream?.close()
                            }catch (e:Exception){

                            }
                        }
                    }
                }
            }
        })
    }

    fun cancelDownload(){
        okHttpClient.value.dispatcher.queuedCalls().forEach {
            it.cancel()
        }
        okHttpClient.value.dispatcher.runningCalls().forEach {
            it.cancel()
        }
    }

}

public interface OnDownloadListener {
    fun onDownloadSuccess(filePath:String)

    fun onDownloadProgress(progress:Int)

    fun onDownloadFail(msg:String)
}