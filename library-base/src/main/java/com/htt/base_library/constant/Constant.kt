package com.gongziyi.appcore.constant

object Constant {



    /**
     * 网络请求 code
     * 对应HTTP的状态码
     */
    const val HTTP_CODE_SUCCESS = 200
    const val HTTP_CODE_LOGOUT = 401

    //已经注册过?
    const val HTTP_CODE_REGISTERED = 418
    const val HTTP_CODE_ERROR = 500
    const val HTTP_CODE_UNAUTHORIZED = 401
    const val HTTP_CODE_FORBIDDEN = 403
    const val HTTP_CODE_NOT_FOUND = 404
    const val HTTP_CODE_REQUEST_TIMEOUT = 408
    const val HTTP_CODE_INTERNAL_SERVER_ERROR = 500
    const val HTTP_CODE_BAD_GATEWAY = 502
    const val HTTP_CODE_SERVICE_UNAVAILABLE = 503
    const val HTTP_CODE_GATEWAY_TIMEOUT = 504


    /**服务器基础地址*/
    var BASE_URL = ""
    /**服务器基础地址*/
    var WS_BASE_URL = ""

}