package com.htt.base_library.network



class ResponseThrowable : Exception {
    var code: Int
    var msg: String

    constructor(error: ERROR):super(error.getValue()) {
        code = error.getKey()
        msg = error.getValue()
    }

    constructor(code: Int, msg: String):super(msg) {
        this.code = code
        this.msg = msg
    }
}

