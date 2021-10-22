package com.htt.base_library.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.htt.base_library.network.BaseResponse
import com.htt.base_library.network.HttpExceptionHandle
import kotlinx.coroutines.*

open class BaseViewModel : ViewModel(), LifecycleObserver {

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

    fun <T> launchGo(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        error: (HttpExceptionHandle.ResponseThrowable) -> Unit = {
        },
        complete: () -> Unit = {},
        isShowDialog: Boolean = true
    ) {
        launchUI {
            handleHttpResult({ withContext(Dispatchers.IO) { block() } },
                { res ->
                    success(res)
                }, {
                    error(it)
                }, {
                    complete()
                    cancel()
                })
        }
    }

    fun <T> launchResult(
        block: suspend CoroutineScope.() -> BaseResponse<T>,
        success: (BaseResponse<T>) -> BaseResponse<T> = {
            it
        },
        error: (HttpExceptionHandle.ResponseThrowable) -> BaseResponse<T> = {
            var response = BaseResponse<T>()
            response.data = null
            response.msg = it.msg ?: ""
            response.code = it.code
            response
        },
        callback: (BaseResponse<T>) -> Unit,
        isShowDialog: Boolean = true
    ) {
        launchUI {

            coroutineScope {
                var response = try {
                    success(withContext(Dispatchers.IO) { block() })
                } catch (e: Throwable) {
                    error(HttpExceptionHandle.handleException(e))
                }
                callback(response)
            }
        }
    }


    private suspend fun <T> handleHttpResult(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(error: HttpExceptionHandle.ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: Throwable) {
                e.printStackTrace()
                error(HttpExceptionHandle.handleException(e))
            } finally {
                complete()
            }
        }
    }

}