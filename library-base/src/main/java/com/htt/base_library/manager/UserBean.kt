package com.htt.base_library.manager

import android.os.Parcelable

/**
 * Created by gongziyi on 2021/5/28 11:58.
 */
class UserBean(
    var token: String,
    var user_id: String,
    var username: String,
    var wstoken: String,
    var is_set_paypwd: Int = 0,
)
