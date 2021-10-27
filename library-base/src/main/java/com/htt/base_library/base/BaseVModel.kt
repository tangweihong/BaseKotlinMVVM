package com.htt.base_library.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gongziyi.appcore.constant.Constant
import com.gongziyi.appcore.manager.UserManager
import com.htt.base_library.network.BaseResponse
import com.htt.base_library.network.ExceptionHandle
import com.htt.base_library.network.HttpExceptionHandle
import com.htt.base_library.network.ResponseThrowable
import kotlinx.coroutines.*



open class BaseVModel : ViewModel(), LifecycleObserver {

    /**
     * 事件
     */
    val ui: UIChange by lazy { UIChange() }

    class UIChange {
        val showDialog by lazy { SingleLiveEvent<String>() }
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
        val errorEvent by lazy { SingleLiveEvent<ResponseThrowable>() }
    }

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param errorCall 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launch(
        block: suspend CoroutineScope.() -> BaseResponse<T>,
        success: (BaseResponse<T>) -> Unit,
        errorCall: ((ResponseThrowable) -> Unit)? = null,
        complete: suspend CoroutineScope.() -> Unit = {},
        isShowDialog: Boolean = true
    ) {
        if (isShowDialog)
            ui.showDialog.call()
        launchUI {
            handleException(

                withContext(Dispatchers.IO) { block },
                { res -> executeResponse(res) {
                    success(it)
                } },
                {
                    errorCall?.invoke(it) ?: ui.errorEvent.postValue(it)
                },
                {
                        ui.dismissDialog.call()
                    complete()
                }
            )
        }
    }

    /**
     *  不过滤请求结果
     * @param block 请求体
     * @param success 成功回调
     * @param errorCall 失败回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param isShowDialog 是否显示加载框
     */
    fun <T> launchNo(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        errorCall: ((ResponseThrowable) -> Unit)? = null,
        complete: suspend CoroutineScope.() -> Unit = {},
        isShowDialog: Boolean = true
    ) {
        if (isShowDialog)
            ui.showDialog.call()
        launchUI {
            handleException(
                withContext(Dispatchers.IO) { block },
                { res -> success(res) },
                { errorCall?.invoke(it) ?: ui.errorEvent.postValue(it) },
                {
                        ui.dismissDialog.call()
                    complete()
                }
            )
        }
    }

    /**
     * 请求体
     */
    private fun launchUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    /**
     * 请求结果过滤
     */
    private suspend fun <T> executeResponse(
        response: BaseResponse<T>,
        success: suspend CoroutineScope.(BaseResponse<T>) -> Unit
    ) {
        coroutineScope {
            when (response.code) {
                Constant.HTTP_CODE_SUCCESS -> success(response)
                Constant.HTTP_CODE_LOGOUT -> UserManager.logOut()
                else -> throw ResponseThrowable(response.code, response.msg ?: "")
            }
        }
    }

    /**
     * 异常统一处理,返回指定数据
     */
    private suspend fun <T> handleException(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: Throwable) {
                error(ExceptionHandle.handleException(e))
            } finally {
                complete()
            }
        }
    }
}