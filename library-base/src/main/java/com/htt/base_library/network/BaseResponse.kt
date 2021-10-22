package com.htt.base_library.network


class BaseResponse<T> {
    var data: T? = null
    var msg: String = ""
    var code: Int = 0
}