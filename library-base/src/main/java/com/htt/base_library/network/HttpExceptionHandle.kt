package com.htt.base_library.network

import android.net.ParseException
import com.google.gson.JsonParseException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

/**
 * Created by NullPointerException on 2020/4/29.
 */
class HttpExceptionHandle {
    companion object{
         val UNAUTHORIZED = 401
         val FORBIDDEN = 403
         val NOT_FOUND = 404
         val REQUEST_TIMEOUT = 408
         val INTERNAL_SERVER_ERROR = 500
         val BAD_GATEWAY = 502
         val SERVICE_UNAVAILABLE = 503
         val GATEWAY_TIMEOUT = 504

         val UNKNOWN_ERROR = 1000
         val PARSE_ERROR=1001 //解析错误
         val NETWORK_ERROR = 1002 //网络错误
         val RUN_ERROR = 1003
         val SSL_ERROR = 1005
         val TIMEOUT_ERROR = 1006

        fun handleException(e:Throwable):ResponseThrowable{
            e.printStackTrace()
            var ex =ResponseThrowable(e)
            return if (e is HttpException) {
                val httpException = e as HttpException
                ex.code=e.code()
                ex.msg="网络错误"
                ex
            } else if (e is RuntimeException) {
                ex.code= RUN_ERROR
                ex.msg="运行时错误"
                ex
            } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {
                ex.code= PARSE_ERROR
                ex.msg = "解析错误"
                ex
            } else if (e is ConnectException) {
                ex.code= NETWORK_ERROR
                ex.msg = "连接失败"
                ex
            } else if (e is SSLHandshakeException) {
                ex.code= SSL_ERROR
                ex.msg = "证书验证失败"
                ex
            } else if (e is ConnectTimeoutException) {
                ex.code= TIMEOUT_ERROR
                ex.msg = "连接超时"
                ex
            } else if (e is SocketTimeoutException) {
                ex.code= TIMEOUT_ERROR
                ex.msg = "连接超时"
                ex
            } else {
                ex.code= UNKNOWN_ERROR
                ex.msg = "未知错误"
                ex
            }
        }
    }



    class ResponseThrowable:Exception{
        var code:Int=0
        var msg:String=""

        constructor(e:Throwable?=null):super(e){
        }

        constructor(error: Int, e: Throwable? = null) : super(e) {
            code = error
            msg = e?.message.toString()
        }

        constructor(code: Int, msg: String, e: Throwable? = null) : super(e) {
            this.code = code
            this.msg = msg
        }
    }
}