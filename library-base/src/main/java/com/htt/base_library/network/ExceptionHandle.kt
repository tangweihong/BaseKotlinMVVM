package com.htt.base_library.network

import android.net.ParseException
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException


object ExceptionHandle {

    fun handleException(e: Throwable?): ResponseThrowable {
        LogUtils.eTag("ExceptionHandle: ", e?.message ?: "none")
        return when (e) {
            is ResponseThrowable -> e

            is ConnectException,
            is HttpException -> ResponseThrowable(ERROR.NETWORD_ERROR)

            is ConnectTimeoutException,
            is java.net.UnknownHostException,
            is java.net.SocketTimeoutException -> ResponseThrowable(ERROR.TIMEOUT_ERROR)

            is JsonParseException,
            is JSONException,
            is ParseException,
            is MalformedJsonException -> ResponseThrowable(ERROR.PARSE_ERROR)

            is javax.net.ssl.SSLException -> ResponseThrowable(ERROR.SSL_ERROR)
            else -> ResponseThrowable(ERROR.UNKNOWN)
        }
    }
}